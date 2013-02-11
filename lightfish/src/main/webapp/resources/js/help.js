$(document).ready(function() {
	$('.page-help').each(function() {
		var $link = $(this);
		var $dialog = $('<div></div>')
			.load($link.attr('href'))
			.dialog({
				autoOpen: false,
				title: $link.attr('title'),
				width: 940,
				height: 'auto',
                                maxHeight: 400,
                                resizable: true
			});

		$link.click(function() {
			$dialog.dialog('open');

			return false;
		});
	});
});

