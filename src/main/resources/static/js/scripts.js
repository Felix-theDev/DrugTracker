// scripts.js
document.addEventListener('DOMContentLoaded', function() {
    const scanQRButton = document.getElementById('scanQRButton');
    const productImageInput = document.getElementById('productImage');
    const uploadForm = document.getElementById('uploadImage');
    const searchForm = document.querySelector('form[action="/search"]');

    // Scan QR Code event
    scanQRButton.addEventListener('click', function() {
        productImageInput.click();
    });

    productImageInput.addEventListener('change', function() {
        if (productImageInput.files.length > 0) {
            const formData = new FormData(uploadForm);
            formData.append('image', productImageInput.files[0]);

            $.ajax({
                url: '/scanQRCode',
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function(response) {
                    if (response.product) {
                        updateProductInfo(response);
                    }
                },
                error: function(xhr) {
                    handleError(xhr);
                }
            });
        }
    });

    // Enter Product Code event
    searchForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const query = searchForm.querySelector('input[name="query"]').value;

        if (query.trim() !== "") {
            $.ajax({
                url: '/search?query=' + encodeURIComponent(query),
                type: 'GET',
                success: function(response) {
                    if (response.product) {
                        updateProductInfo(response);
                    } else {
                        handleError({ status: 404, responseText: '{"message": "Product not found."}' });
                    }
                },
                error: function(xhr) {
                    handleError(xhr);
                }
            });
        }
    });

    // Function to update the product information dynamically
    function updateProductInfo(response) {
        const productInfo = document.getElementById('productInfo');
        productInfo.innerHTML = `
            <h2 class="text-center mb-4">Product Information</h2>
            <div class="card shadow-sm mb-4">
                <div class="card-header text-white bg-info">
                    Product Details
                </div>
                <div class="card-body">
                    <p><strong>Product Name:</strong> ${response.product.name}</p>
                    <p><strong>Manufacturing Date:</strong> ${response.product.manufactureDate}</p>
                    <p><strong>Expiry Date:</strong> ${response.product.expiryDate}</p>
                </div>
            </div>

            <h3 class="text-center mb-4">Timeline Journey</h3>
            <div class="timeline-container">
                ${response.blockDetails.map(block => `
                    <div class="timeline-item mb-4 p-3 border rounded">
                        <p><strong>User:</strong> ${block.username}</p>
                        <p><strong>Role:</strong> ${block.role}</p>
                        <p><strong>Timestamp:</strong> ${block.timeline}</p>
                    </div>
                `).join('')}
            </div>
        `;
    }

    // Function to handle errors
    function handleError(xhr) {
        const productInfo = document.getElementById('productInfo');
        const response = JSON.parse(xhr.responseText);

        if (xhr.status === 400 && response.message === "QR code not found in the image.") {
            productInfo.innerHTML = `
                <div class="alert alert-warning" role="alert">
                    The uploaded file is not a valid QR code. Please upload a valid QR code image.
                </div>
            `;
        } else if (xhr.status === 404 && response.message === "Product not found.") {
            productInfo.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    Alert! This product is either not original or has not been registered in our system.
                </div>
            `;
        } else {
            productInfo.innerHTML = `
                <div class="alert alert-danger" role="alert">
                    An unexpected error occurred while processing the image. Please try again.
                </div>
            `;
        }
    }
});
