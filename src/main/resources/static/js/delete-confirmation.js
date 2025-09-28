function confirmDelete(button) {
  var dataID = button.getAttribute("data-id");
  var endpoint = button.getAttribute("data-endpoint");

  var confirmation = confirm(
    "Você tem certeza que deseja excluir este registro?"
  );

  if (confirmation) {
    window.location.href = "/" + endpoint + "/delete/" + dataID;
  }
}
