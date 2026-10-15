
document.addEventListener("DOMContentLoaded", () => {

    // 1. Confirmation avant suppression
    const deleteForms = document.querySelectorAll(
        "[data-confirm-delete]"
    );

    deleteForms.forEach((form) => {
        form.addEventListener("submit", (event) => {

            const taskTitle = form.dataset.taskTitle || "";

            const confirmMessage =
                form.dataset.confirmMessage || "";

            const warningMessage =
                form.dataset.warningMessage || "";

            const message = `${confirmMessage}\n\n${warningMessage}`;

            const confirmed = window.confirm(message);

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });

    // 2. Masquer automatiquement les messages de succès
    const successAlerts = document.querySelectorAll(
        ".alert-success"
    );

    successAlerts.forEach((alert) => {
        window.setTimeout(() => {

            alert.classList.add("alert-hidden");

            window.setTimeout(() => {
                alert.remove();
            }, 300);

        }, 5000);
    });

    // 3. Empêcher les doubles soumissions
    const taskForms = document.querySelectorAll(
        ".form-card form"
    );

    taskForms.forEach((form) => {
        form.addEventListener("submit", (event) => {

            if (form.dataset.submitting === "true") {
                event.preventDefault();
                return;
            }

            // La validation est gérée par Spring Boot
            // lorsque le formulaire utilise novalidate.
            if (!form.noValidate && !form.checkValidity()) {
                return;
            }

            form.dataset.submitting = "true";

            const submitButton = form.querySelector(
                'button[type="submit"]'
            );

            if (submitButton) {

                const savingMessage =
                    form.dataset.savingMessage || "Saving...";

                submitButton.disabled = true;
                submitButton.classList.add("btn-loading");
                submitButton.textContent = savingMessage;
            }
        });
    });

});
