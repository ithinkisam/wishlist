$(function() {

	$('#delete-wish-modal').on('show.bs.modal', function(e) {
		var a = $(e.relatedTarget);
		var wishId = a.data('wish-id');
		$('#delete-wish-id').val(a.data('wish-id'));
		$('#delete-wish-description').html(a.data('wish-description'));
		console.log(a.data('wish-description'));
	});
	
});
