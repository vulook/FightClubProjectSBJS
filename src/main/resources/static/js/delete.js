// Function to delete a user by ID
async function deleteUser(id, modal) {
    try {
        // Fetch user information for the specified ID
        let getOneUser = await fetch('/fight-club/admin/' + id, { method: 'GET' });
        let json = await getOneUser.json();

        // Set modal title and footer
        document.getElementById("modalTitle").innerHTML = "Delete user";
        document.getElementById("modal-footer").innerHTML =
            `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
             <button type="button" class="btn btn-info" id="modalBtn">Delete</button>`;

        // Build HTML content for the user information in the modal
        let html = `
        <form id="deleteForm">
            <div class="d-flex flex-column align-items-center">
                <div class="mb-3">
                    <h6 class="text-dark fw-bold">ID:</h6>
                    <input disabled style="width: 400px;" class="form-control" type="text" name="username" value="${json.id}">
                </div>
                <div class="mb-3">
                    <h6 class="text-dark fw-bold">Username:</h6>
                    <input disabled style="width: 400px;" class="form-control" type="text" name="username" value="${json.username}">
                </div>
                <div class="mb-3">
                    <h6 class="text-dark fw-bold">BirthDate:</h6>
                    <input disabled style="width: 400px;" class="form-control" type="date" name="birthDate" value="${json.birthDate}">
                </div>
                <div class="mb-3">
                    <h6 class="text-dark fw-bold">Email:</h6>
                    <input disabled style="width: 400px;" class="form-control" type="text" name="email" value="${json.email}">
                </div>
                <div class="mb-3">
                    <h6 class="text-dark fw-bold">Role:</h6>
                    <select disabled style="width: 400px;" class="form-select" multiple name="listRoles" required="required">
                        <option value="ADMIN">ADMIN</option>
                        <option selected="selected" value="USER">USER</option>
                    </select>
                </div>
            </div>
        </form>
        `;

        // Display the user information in the modal body
        let myModal = document.querySelector('#modal-body');
        myModal.innerHTML = html;

        // Event listener for the delete button in the modal
        document.getElementById("modalBtn").addEventListener('click', async function (evt) {
            // Send a DELETE request to delete the user
            const deleteUser = await fetch('/fight-club/admin/' + id, {
                method: 'DELETE',
                headers: {
                    'Content-type': 'application/json',
                    'Accept': 'application/json',
                    'Referer': null
                }
            });

            if (deleteUser.ok) {
                // If successful, update the users table and hide the modal
                await getAllUsers();
                modal.modal('hide');
            }
        });

    } catch (error) {
        console.error('Error during user deletion:', error);
        alert('Error deleting user. See console for details.');
    }
}