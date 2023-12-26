$("#update-position").on('show.bs.modal', function(e) {

    //get data attributes of the clicked element
    $("#update-ticker").attr('value', $(e.relatedTarget).data('ticker'));
    $("#update-qty").attr('value', $(e.relatedTarget).data('qty'));
    $("#update-cost").attr('value', $(e.relatedTarget).data('cost'));
});
