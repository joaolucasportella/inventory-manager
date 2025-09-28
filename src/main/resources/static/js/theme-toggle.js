function toggleClass(element, class1, class2) {
  if (element.classList.contains(class1)) {
    element.classList.remove(class1);
    element.classList.add(class2);
  } else {
    element.classList.remove(class2);
    element.classList.add(class1);
  }
}

function setTheme(theme) {
  const body = document.getElementById("page-body");
  const mainContent = document.getElementById("main-content");
  const listGroupItems = document.querySelectorAll(".list-group-item");
  const links = document.querySelectorAll(".list-group-item a");
  const tables = document.querySelectorAll(".table");
  const inputs = document.querySelectorAll("input, select, option");

  if (theme === "dark") {
    body.classList.remove("bg-light", "text-dark");
    body.classList.add("bg-dark", "text-light");

    if (mainContent) {
      mainContent.classList.remove("bg-light", "text-dark");
      mainContent.classList.add("bg-dark", "text-light");
    }

    listGroupItems.forEach((item) => {
      item.classList.remove("bg-light", "text-dark");
      item.classList.add("bg-dark", "text-light");
    });

    links.forEach((link) => {
      link.classList.remove("text-dark");
      link.classList.add("text-light");
    });

    tables.forEach((table) => {
      table.classList.remove("table-light");
      table.classList.add("table-dark");
    });

    inputs.forEach((input) => {
      input.classList.remove("bg-light", "text-dark");
      input.classList.add("bg-dark", "text-light");
    });
  } else {
    body.classList.remove("bg-dark", "text-light");
    body.classList.add("bg-light", "text-dark");

    if (mainContent) {
      mainContent.classList.remove("bg-dark", "text-light");
      mainContent.classList.add("bg-light", "text-dark");
    }

    listGroupItems.forEach((item) => {
      item.classList.remove("bg-dark", "text-light");
      item.classList.add("bg-light", "text-dark");
    });

    links.forEach((link) => {
      link.classList.remove("text-light");
      link.classList.add("text-dark");
    });

    tables.forEach((table) => {
      table.classList.remove("table-dark");
      table.classList.add("table-light");
    });

    inputs.forEach((input) => {
      input.classList.remove("bg-dark", "text-light");
      input.classList.add("bg-light", "text-dark");
    });
  }
}

function toggleTheme() {
  const body = document.getElementById("page-body");
  const theme = body.classList.contains("bg-dark") ? "light" : "dark";
  setTheme(theme);
  localStorage.setItem("theme", theme);
}

document.getElementById("theme-toggle").addEventListener("click", toggleTheme);

document.addEventListener("DOMContentLoaded", function () {
  const savedTheme = localStorage.getItem("theme") || "dark";
  setTheme(savedTheme);
});
