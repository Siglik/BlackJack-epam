$("a.rank-btn").click(function(event) {
	var id = $(event.target).attr('data-user-id');
	$(event.target).prop("disabled", true );
	$.getJSON('changeplayertype' + '?id=' + id).done(function(data) {
		$( "#x" ).prop( "disabled", false );
		if (data.result === "OK") {
			$('#rank').text(data.rank);
		} else {
			alert(data.result + "\n" + data.message)
		}
	});
	return false;
});