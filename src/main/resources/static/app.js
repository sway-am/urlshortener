async function loadStats(shortUrl) {
    const statsDiv = document.getElementById("stats");
    statsDiv.innerHTML = `
        <div class="loading">
            <div class="spinner"></div>
            <p>Loading stats...</p>
        </div>
    `;

    const code = shortUrl.split("/").pop();

    try {
        const response = await fetch(`/api/stats/${code}`);
        const data = await response.json();

        if (!response.ok) {
            statsDiv.innerHTML = `<p style="color: #f56565; text-align: center;">${data.message || "Failed to load stats"}</p>`;
            return;
        }

        statsDiv.innerHTML = `
            <div class="stats-grid">
                <div class="stat-item">
                    <div class="stat-label">Clicks</div>
                    <div class="stat-value">${data.clickCount}</div>
                </div>
                <div class="stat-item">
                    <div class="stat-label">Created</div>
                    <div class="stat-value">${new Date(data.createdAt).toLocaleDateString()}</div>
                </div>
                <div class="stat-item">
                    <div class="stat-label">Expires</div>
                    <div class="stat-value">${data.expireAt ? new Date(data.expireAt).toLocaleDateString() : "Never"}</div>
                </div>
            </div>
            <div class="stat-item" style="margin-top: 12px;">
                <div class="stat-label">Original URL</div>
                <div class="stat-value" style="font-size: 12px; word-break: break-all;">${data.originalUrl}</div>
            </div>
        `;
    } catch (err) {
        statsDiv.innerHTML = '<p style="color: #f56565; text-align: center;">Unable to fetch stats</p>';
    }
}

async function shortenUrl() {
    const input = document.getElementById("urlInput");
    const resultDiv = document.getElementById("result");
    const errorDiv = document.getElementById("error");

    resultDiv.innerHTML = "";
    errorDiv.innerHTML = "";

    const url = input.value.trim();

    if (!url) {
        errorDiv.innerText = "Please enter a URL.";
        return;
    }

    try {
        const response = await fetch("/api/shorten", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ url })
        });

        const data = await response.json();

        if (!response.ok) {
            errorDiv.innerText = data.message || "Something went wrong";
            return;
        }

        const shortUrl = data.shortUrl;

        resultDiv.innerHTML = `
            <div class="result-card">
                <div class="url-display">
                    <label>Short URL:</label>
                    <a href="${shortUrl}" target="_blank" id="shortUrlLink">${shortUrl}</a>
                    <button class="btn-copy" onclick="copyToClipboard('${shortUrl}', this)">Copy</button>
                </div>
                <button class="btn-stats" onclick="loadStats('${shortUrl}')">View Statistics</button>
                <div id="stats"></div>
            </div>
        `;
    } catch (err) {
        errorDiv.innerText = "Server not reachable.";
    }
}

function copyToClipboard(text, buttonElement) {
    navigator.clipboard.writeText(text).then(() => {
        const originalText = buttonElement.innerText;
        buttonElement.innerText = "Copied!";
        buttonElement.style.background = "#48bb78";
        buttonElement.style.color = "white";
        buttonElement.style.borderColor = "#48bb78";
        
        setTimeout(() => {
            buttonElement.innerText = originalText;
            buttonElement.style.background = "white";
            buttonElement.style.color = "#667eea";
            buttonElement.style.borderColor = "#667eea";
        }, 2000);
    }).catch((err) => {
        console.error("Copy failed:", err);
        alert("Failed to copy to clipboard");
    });
}