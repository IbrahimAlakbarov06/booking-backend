document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('bookingForm');
    const result = document.getElementById('result');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const data = {
            flightId: form.flightId.value,
            passengerId: form.passengerId.value,
            numberOfSeats: form.numberOfSeats.value
        };

        try {
            const res = await fetch('/api/v1/bookings', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            if (!res.ok) {
                const err = await res.json();
                result.innerHTML = `<p style="color:red;">❌ ${err.message}</p>`;
            } else {
                const booking = await res.json();
                result.innerHTML = `<p style="color:green;">✅ Booking #${booking.bookingId} created!</p>`;
                form.reset();
            }

        } catch (error) {
            result.innerHTML = `<p style="color:red;">❌ Error: ${error.message}</p>`;
        }
    });
});
