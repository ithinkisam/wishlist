var test = {};

function formatReferenceRow(ref, wishId, isEven) {
	var row = $('<tr>', {
		class: isEven ? 'even' : 'odd wl-odd-darken'
	});
	row.append($('<td>', {
		class: 'text-muted pl-5',
		html: '<a href="' + ref.url + '" target="_blank">' + ref.url + ' <i class="far fa-external-link"></i></a>'
	}));
	row.append($('<td><button class="btn btn-xs btn-danger delete-ref" data-reference-id="' + ref.id + '">Remove</button></td>'));
	row.append($('<td>'));
	row.append($('<td>'));
	return row;
}

function formatNewReferenceRow(wishId, isEven) {
	var row = $('<tr>', {
		class: isEven ? 'even' : 'odd wl-odd-darken'
	});
	row.append($('<td>', {
		class: 'pl-5',
		html: '<input type="text" placeholder="URL" class="form-control form-control-sm wish-reference" style="height: calc(2rem + 2px)">'
	}));
	row.append($('<td><button class="btn btn-xs btn-primary add-ref" data-wish-id="' + wishId + '">Add</button></td>'));
	row.append($('<td>'));
	row.append($('<td>'));
	return row;
}

$(function() {

	var initReferenceDropdowns = function() {
		$('.show-refs').click(function(e, config) {
			var showRefsLink = $(this);
			var tr = $(this).parents('tr');
			var row = $('#wish-table').DataTable().row(tr);
			var wishId = $(this).data('wish-id');
			
			$.get("/wish/" + wishId, function(wish) {
				if (row.child.isShown()) {
					row.child.hide();
					tr.removeClass('shown');
				} else {
					var rows = [];
					wish.references.forEach(function(ref, index) {
						rows.push(formatReferenceRow(ref, wishId, tr.hasClass('even')));
					});
					rows.push(formatNewReferenceRow(wishId, tr.hasClass('even')));
					row.child(rows).show();
					row.child().find('button.delete-ref').click(function() {
						var refId = $(this).data('reference-id');
						$.post("/references/" + refId + "/delete", function(result) {
							if (result == "OK") {
								$('button.delete-ref[data-reference-id=' + refId + ']').parents('tr').remove();
							}
						});
					});
					row.child().find('button.add-ref').click(function() {
						var addRefButton = $(this);
						var wishId = addRefButton.data('wish-id');
						var url = addRefButton.parents('tr').find('input').val();
						$.post("/wish/" + wishId + "/references", {
							'url': url
						}, function(newRef) {
							var newRow = formatReferenceRow(newRef, wishId, tr.hasClass('even'));
							newRow.find('button.delete-ref').click(function() {
								$.post("/references/" + newRef.id + "/delete", function(deleteResult) {
									if (deleteResult == "OK") {
										$('button.delete-ref[data-reference-id=' + newRef.id + ']').parents('tr').remove();
									}
								});
							});
							newRow.insertBefore($(row.child()[row.child().length-1]));
							addRefButton.parents('tr').find('input').removeClass('is-invalid');
							addRefButton.parents('tr').find('input').val('');
							addRefButton.parents('tr').find('.invalid-feedback').remove();
						}).fail(function(error) {
							addRefButton.parents('tr').find('input').addClass('is-invalid');
							$('<div class="invalid-feedback">Please provide a valid URL</div>').insertAfter(addRefButton.parents('tr').find('input'));
						});
					});
					tr.addClass('shown');
				}
			});
		});
	}
	
	$('#wish-table').DataTable({
		ordering: false,
		paging: false,
		stripeClasses: [ 'odd wl-odd-darker', 'even' ],
		language: {
			zeroRecords: "Either you don't want anything or you haven't figured out this website yet..."
		},
		initComplete: initReferenceDropdowns
	});

	$('#edit-wish-modal').on('show.bs.modal', function(e) {
		var a = $(e.relatedTarget);
		var wishId = a.data('wish-id');
		$('#edit-wish-id').val(a.data('wish-id'));
		
		$.get("/wish/" + wishId, function(wish) {
			$('#edit-wish-description').val(wish.description);
			$('#edit-wish-description-orig').val(wish.description);
			var priceVal = wish.price == null ? "" : wish.price.range;
			$('#edit-wish-price').val(priceVal);
			$('#edit-wish-price-orig').val(priceVal);
		});
	});
	
	$('#edit-wish-description, #edit-wish-price').on('keyup', function() {
		var isDescriptionChanged = $('#edit-wish-description').val() != $('#edit-wish-description-orig').val();
		var isPriceChanged = $('#edit-wish-price').val() != $('#edit-wish-price-orig').val();
		$('#edit-wish-button').attr('disabled', !isDescriptionChanged && !isPriceChanged);
	});
	
	$('#delete-wish-modal').on('show.bs.modal', function(e) {
		var a = $(e.relatedTarget);
		var wishId = a.data('wish-id');
		$('#delete-wish-id').val(a.data('wish-id'));
		$('#delete-wish-description').html(a.data('wish-description'));
	});
	
});
