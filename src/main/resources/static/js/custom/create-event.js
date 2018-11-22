$(function() {
	
	$('#new-event-date').flatpickr({
		enableTime: true,
		dateFormat: "Y-m-d\\TH:i:S.000\\Z",
		minDate: new Date(),
		altInput: true,
		altFormat: "F j, Y h:iK"
	});
	
});
