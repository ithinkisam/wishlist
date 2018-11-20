$(function() {

	$.fn.dataTable.ext.search.push(
		function(settings, searchData, index, rowData, counter) {
			console.log("settings", settings);
			console.log("rowData", rowData);
			return true;
		}
	);
	
	$('.wish-table').DataTable({
		ordering: false,
		searching: false,
		paging: false,
		language: {
			zeroRecords: "This user has not created any wishes for this event"
		}
	});
	
	$('#toggle-purchased-wishes').click(function() {
		if ($(this)[0].checked) {
			$('.wish-table').DataTable().search('hide');
		} else {
			$('.wish-table').DataTable().search('');
		}
	});
	
});
