console.log("✅ delete-cinema.js loaded successfully!");

function deleteCinema(cinemaId) {
    if (confirm(i18n.confirmDelete)) {
        fetch(`/admin/cinemas/${cinemaId}`, { method: "DELETE" })
            .then(r => {
                if (r.ok) {
                    // Server sẽ tự quyết định soft hay hard
                    r.json().then(res => {
                        if (res.type === "hard") {
                            alert(i18n.successHard);
                        } else {
                            alert(i18n.successSoft);
                        }
                        window.location.href = "/admin/cinemas";
                    }).catch(() => {
                        // Nếu server không trả JSON, fallback
                        alert(i18n.successDelete);
                        window.location.href = "/admin/cinemas";
                    });
                } else {
                    r.json().then(err =>
                        alert(i18n.errorDelete.replace("{0}", err.error || "Unknown error"))
                    );
                }
            })
            .catch(err => alert(i18n.errorNetwork.replace("{0}", err)));
    }
}
