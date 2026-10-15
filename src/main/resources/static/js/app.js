
document.addEventListener("DOMContentLoaded", () => {

    // 1. Confirmation avant suppression
    const deleteForms = document.querySelectorAll("[data-confirm-delete]");

    deleteForms.forEach((form) => {
        form.addEventListener("submit", (event) => {
            const taskTitle = form.dataset.taskTitle || "cette tâche";

            const confirmed = window.confirm(
                `Voulez-vous vraiment supprimer « ${taskTitle} » ?\n\nCette action est irréversible.`
            );

            if (!confirmed) {
                event.preventDefault();
            }
        });
    });

    // 2. Masquer automatiquement les messages de succès
    const successAlerts = document.querySelectorAll(".alert-success");

    successAlerts.forEach((alert) => {
        window.setTimeout(() => {
            alert.classList.add("alert-hidden");

            window.setTimeout(() => {
                alert.remove();
            }, 300);
        }, 5000);
    });

    // 3. Empêcher les doubles soumissions
    const taskForms = document.querySelectorAll(".form-card form");

    taskForms.forEach((form) => {
        form.addEventListener("submit", (event) => {
            if (form.dataset.submitting === "true") {
                event.preventDefault();
                return;
            }

            // Laisser le navigateur valider les champs HTML
            if (!form.checkValidity()) {
                return;
            }

            form.dataset.submitting = "true";

            const submitButton = form.querySelector('button[type="submit"]');

            if (submitButton) {
                submitButton.disabled = true;
                submitButton.classList.add("btn-loading");
                submitButton.textContent = "Enregistrement...";
            }
        });
    });

});
