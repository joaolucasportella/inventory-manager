function submitSearchForm(form) {
  clearTimeout(form.submitTimeout);
  form.submitTimeout = setTimeout(() => form.submit(), 800);
}
