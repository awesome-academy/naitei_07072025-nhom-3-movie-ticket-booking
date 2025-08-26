function initRevenueCharts(dailyLabels, dailyData, monthlyLabels, monthlyData, yearlyLabels, yearlyData) {

    dailyData = dailyData.map(Number);
    monthlyData = monthlyData.map(Number);
    yearlyData = yearlyData.map(Number);

    console.log("Daily:", dailyLabels, dailyData);
    console.log("Monthly:", monthlyLabels, monthlyData);
    console.log("Yearly:", yearlyLabels, yearlyData);

    const dailyChart = new Chart(document.getElementById("dailyChart"), {
        type: 'bar',
        data: {
            labels: dailyLabels,
            datasets: [{
                label: 'Doanh thu theo ngày',
                data: dailyData,
                backgroundColor: 'rgba(54, 162, 235, 0.6)'
            }]
        }
    });

    const monthlyChart = new Chart(document.getElementById("monthlyChart"), {
        type: 'bar',
        data: {
            labels: monthlyLabels,
            datasets: [{
                label: 'Doanh thu theo tháng',
                data: monthlyData,
                backgroundColor: 'rgba(75, 192, 192, 0.6)'
            }]
        }
    });

    const yearlyChart = new Chart(document.getElementById("yearlyChart"), {
        type: 'bar',
        data: {
            labels: yearlyLabels,
            datasets: [{
                label: 'Doanh thu theo năm',
                data: yearlyData,
                backgroundColor: 'rgba(255, 159, 64, 0.6)'
            }]
        }
    });

    const chartTypeSelect = document.getElementById("chartType");
    chartTypeSelect.addEventListener("change", function() {
        document.getElementById("dailyContainer").style.display   = "none";
        document.getElementById("monthlyContainer").style.display = "none";
        document.getElementById("yearlyContainer").style.display  = "none";

        if (this.value === "daily") {
            document.getElementById("dailyContainer").style.display = "block";
        } else if (this.value === "monthly") {
            document.getElementById("monthlyContainer").style.display = "block";
        } else if (this.value === "yearly") {
            document.getElementById("yearlyContainer").style.display = "block";
        }
    });

    // default -> daily
    chartTypeSelect.value = "daily";
    document.getElementById("dailyContainer").style.display = "block";
}
