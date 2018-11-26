$(function() {

	$('.wish-table').dataTable({
		ordering: false,
		searching: true,
		paging: false,
		stripeClasses: [ 'odd wl-odd-darker', 'even' ],
		language: {
			zeroRecords: "This user has not created any wishes for this event"
		}
	});
	
	$('#toggle-purchased-wishes').on('change', function() {
		if ($(this)[0].checked) {
			$('tr.purchased').hide();
		} else {
			$('tr').show();
		}
	});
	$('#toggle-purchased-wishes').trigger('change');
	
	var checkHideToggle = function() {
		if ($('.manage-event-link').hasClass('active')) {
			$('#hide-purchases').hide();
		} else {
			$('#hide-purchases').show();
		}
	}
	$('.event-nav-link').on('shown.bs.tab', checkHideToggle);
	checkHideToggle();
	
	$('.purchase-btn').click(function() {
		var wishId = $(this).data('wish-id');
		var tr = $(this).parents('tr');
		var tableId = $(this).parents('table').attr('id');
		$.post('/api/wishes/' + wishId + '/purchase', function(wish) {
			var clone = tr.clone(true);
			clone.hide();
			tr.hide('normal').remove();
			clone.addClass('purchased table-success');
			clone.find('.return-btn').show();
			clone.find('.purchase-btn').hide();
			$('#' + tableId).find('tbody').append(clone);
			clone.show('normal');
		});
	});
	
	$('.return-btn').click(function() {
		var wishId = $(this).data('wish-id');
		var tr = $(this).parents('tr');
		var tableId = $(this).parents('table').attr('id');
		$.post('/api/wishes/' + wishId + '/unpurchase', function(wish) {
			var clone = tr.clone(true);
			clone.hide();
			tr.hide('normal').remove();
			clone.removeClass('purchased table-success');
			clone.find('.purchase-btn').show();
			clone.find('.return-btn').hide();
			$('#' + tableId).find('tbody').prepend(clone);
			clone.show('normal');
		});
	});
	
	$('#edit-event-date').flatpickr({
		enableTime: true,
		dateFormat: "Y-m-d\\TH:i:S.000\\Z",
		minDate: new Date(),
		altInput: true,
		altFormat: "F J, Y h:iK"
	});
	
	$('#event-member-table').dataTable({
		searching: false,
		ordering: false,
		paging: false,
		stripeClasses: [ 'odd wl-odd-darker', 'even' ],
	});

	$('#event-admin-table').dataTable({
		searching: false,
		ordering: false,
		paging: false,
		stripeClasses: [ 'odd wl-odd-darker', 'even' ],
	});
	
	$('#add-event-member-form').submit(function(e) {
		e.preventDefault();
		
		var eventId = $(this).data('event-id');
		$.post('/events/' + eventId + '/members', {
			'email': $('#event-member-email').val()
		}, function(member) {
			var alert = $('<div>', {
				'class': 'alert alert-success',
				'role': 'alert'
			});
			alert.text("We've added " + member.firstName + " to this event! Refresh the page to see the changes.");
			$('#event-member-email').val('');
			$('#manage-member-message-container').html(alert);
		}).fail(function(response) {
			var alert = $('<div>', {
				'class': 'alert alert-danger',
				'role': 'alert'
			});
			alert.text(response.responseJSON.error);
			$('#manage-member-message-container').html(alert);
		});
		
		return false;
	});

	$('#add-event-admin-form').submit(function(e) {
		e.preventDefault();
		
		var eventId = $(this).data('event-id');
		$.post('/events/' + eventId + '/admins', {
			'email': $('#event-admin-email').val()
		}, function(admin) {
			var alert = $('<div>', {
				'class': 'alert alert-success',
				'role': 'alert'
			});
			alert.text("We've added " + admin.firstName + " to this event! Refresh the page to see the changes.");
			$('#event-admin-email').val('');
			$('#manage-admin-message-container').html(alert);
		}).fail(function(response) {
			var alert = $('<div>', {
				'class': 'alert alert-danger',
				'role': 'alert'
			});
			alert.text(response.responseJSON.error);
			$('#manage-admin-message-container').html(alert);
		});
		
		return false;
	});
	
	$('.btn-remove-member').click(function() {
		var row = $(this).parents('tr');
		var eventId = $(this).data('event-id');
		var userId = $(this).data('user-id');
		$.post('/events/' + eventId + '/members/delete', {
			'id': userId
		}, function(result) {
			if (result == "OK") {
				var alert = $('<div>', {
					'class': 'alert alert-success',
					'role': 'alert'
				});
				alert.text("The user has been successfully removed from this event!");
				$('#manage-member-message-container').html(alert);
				row.remove();
				$('a[href="#event-member-' + userId + '"]').parent().remove();
			}
		}).fail(function(response) {
			var alert = $('<div>', {
				'class': 'alert alert-danger',
				'role': 'alert'
			});
			alert.text(response.responseJSON.error);
			$('#manage-member-message-container').html(alert);
		});
	});

	$('.btn-remove-admin').click(function() {
		var row = $(this).parents('tr');
		var eventId = $(this).data('event-id');
		var userId = $(this).data('user-id');
		$.post('/events/' + eventId + '/admins/delete', {
			'id': userId
		}, function(result) {
			if (result == "OK") {
				var alert = $('<div>', {
					'class': 'alert alert-success',
					'role': 'alert'
				});
				alert.text("The user has been successfully removed from this event!");
				$('#manage-admin-message-container').html(alert);
				row.remove();
				$('a[href="#event-admin-' + userId + '"]').parent().remove();
			}
		}).fail(function(response) {
			var alert = $('<div>', {
				'class': 'alert alert-danger',
				'role': 'alert'
			});
			alert.text(response.responseJSON.error);
			$('#manage-admin-message-container').html(alert);
		});
	});
	
	$('#add-secret-santa-rule-btn').click(function(e) {
		e.preventDefault();
		var newRule = $('#secret-santa-rule-template').clone();
		newRule.removeAttr('id');
		newRule.insertBefore($('#add-secret-santa-rule-btn'));
		newRule.show('normal');
		return false;
	});
	
	$('a[href="#event-manage-secret-santa"]').on('shown.bs.tab', function() {
		$('#secret-santa-draws-table').DataTable({
			paging: false,
			searching: false,
			ordering: true,
			order: [[1, "asc"]],
			columnDefs: [
				{ "orderable": false, "targets": 0 }
			]
		});
	});
	
});
