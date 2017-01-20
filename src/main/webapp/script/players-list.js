$("a.ban-btn").click(function(event) {
	var id = $(event.target).closest('tr').attr('data-user-id');
	var operation = null;
	if ($(event.target).hasClass("ban")) {
		var operation = "ban";
	} else if ($(event.target).hasClass("unban")) {
		var operation = "unban";
	}
	$.getJSON('player' + operation + '?id=' + id).done(function(data) {
		if (data.result === "OK") {
			$(event.target).toggleClass("ban unban")
			if (operation === "ban") {
				$(event.target).text(buttons.unban);
			} else {
				$(event.target).text(buttons.ban);
			}

		} else {
			alert(data.result + "\n" + data.message)
		}
	});
	return false;
});

$("tr.data-row").click(function ban(event) {
	if (!$(event.target).hasClass('ban-btn')) {
		var id = $(event.target).closest('tr').attr('data-user-id');
		window.location.href = "playerinfo?id="+id;
	}
	return false;
});