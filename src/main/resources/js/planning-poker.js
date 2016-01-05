function showDialog(id, title, message) {
    var dialog = new AJS.Dialog({
        width:400,
        height:200,
        id: id + "-dialog",
        closeOnOutsideClick: true
    });
    dialog.addHeader(title);
    dialog.addPanel("Panel 1", "<p>" + message + "</p>", "panel-body");
    dialog.addButton("Confirm", function (dialog) {
        dialog.remove();
        AJS.$("#" + id + "-link").click();
    });
    dialog.addLink("Cancel", function (dialog) {
        dialog.remove();
    }, "#");

    dialog.show();
}

AJS.$(document).ready(function() {
    AJS.$(".more").collapseText({showChars: '40'});
});