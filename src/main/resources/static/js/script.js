document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('form');

    forms.forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            const formData = new FormData(form);
            const data = Object.fromEntries(formData.entries());

            const urlMap = {
                'flightForm': '/api/v1/flights',
                'bookingForm': '/api/v1/bookings',
                'passengerForm': '/api/v1/passengers'
            };

            const resultDiv = form.nextElementSibling;
            const apiUrl = urlMap[form.id];

            try {
                const res = await fetch(apiUrl, {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });

                if (!res.ok) {
                    const err = await res.json();
                    resultDiv.innerHTML = `<p style="color:red;">❌ ${err.message || 'Error occurred!'}</p>`;
                } else {
                    const responseData = await res.json();
                    resultDiv.innerHTML = `<p style="color:green;">✅ Successfully created!</p>`;
                    form.reset();
                }
            } catch (error) {
                resultDiv.innerHTML = `<p style="color:red;">❌ ${error.message}</p>`;
            }
        });
    });
});
