// Function to edit a user by ID
async function editUser(id, modal) {
    let myModal = document.querySelector('#modal-body');
    let getOneUser = await fetch(`/fight-club/admin/${id}`);
    let json = await getOneUser.json();
    document.getElementById("modalTitle").innerHTML = "Edit user";
    document.getElementById("modal-footer").innerHTML =
        `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
         <button type="submit" class="btn btn-info" id="modalBtn">Edit</button>`;

    // Build HTML content for the user information in the modal
    let htmlEdit = `
        <form id="editUser">
           <div class="d-flex flex-column align-items-center">
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">ID:</h6>
                   <input type="text" name="editId" style="width: 400px;" class="form-control" id="editId" value="${json.id}" disabled>
               </div>
               
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">Username:</h6>
                   <input name="editUsername" style="width: 400px;" class="form-control" type="text" id="editUsername"  value="${json.username}">
               </div>
               
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">BirthDate:</h6>
                   <input name="editBirthDate" style="width: 400px;" class="form-control" type="date" id="editBirthDate" value="${json.birthDate}">
               </div>
               
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">Email:</h6>
                   <input name="editEmail" style="width: 400px;" class="form-control" type="text" id="editEmail" value="${json.email}">
               </div>
               
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">Password:</h6>
                   <input name="editPassword" style="width: 400px;" class="form-control" type="password" id="editPpassword" value="${json.password}">
               </div>
               
               <div class="mb-3">
                   <h6 class="text-dark fw-bold">Role:</h6>
                   <select style="width: 400px;" id="editRole" class="form-select" multiple required="required">
                       <option value="ADMIN" ${json.roles.some(role => role.roleName === "ADMIN") ? 'selected' : ''}>ADMIN</option>
                       <option value="USER" ${json.roles.some(role => role.roleName === "USER") ? 'selected' : ''}>FIGHTER</option>
                   </select>
               </div>
               
           </div>
       </form>`;

    // Display the user information in the modal body
    myModal.innerHTML = htmlEdit;

    // Set the selected roles
    let roleSelect = document.querySelector('#editRole');
    json.roles.forEach(role => {
        let option = roleSelect.querySelector(`option[value="${role.roleName}"]`);
        if (option) {
            option.selected = true;
        }
    });

    document.getElementById("modalBtn").addEventListener('click', async function (evt) {
        evt.preventDefault()
        let editForm = document.querySelector('#editUser');
        let id = editForm.editId.value;
        let username = editForm.editUsername.value;
        let birthDate = editForm.editBirthDate.value;
        let email = editForm.editEmail.value;
        let password = editForm.editPassword.value;

        // Get roles
        let getRole = () => {
            let array = [];
            let role = document.querySelector('#editRole');
            for (let i = 0; i < role.length; i++) {
                if (role[i].selected) {
                    array.push(role[i].value);
                }
            }

            return array;
        };

        let editUser = {
            id: id,
            username: username,
            birthDate: birthDate,
            email: email,
            password: password,
            roles: getRole()
        }

        // Send a PUT request to update the user
        let update = await fetch(`/fight-club/admin/${id}`, {
            method: 'PUT',
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json',
            },

            body: JSON.stringify(editUser)
        });

        if (update.ok) {
            await getAllUsers();
            modal.modal('hide');
            console.log('User details updated successfully');
            alert('User details updated successfully');

        } else {
            // Handle validation errors
            let errors = await update.json();
            console.error('Failed to update user details. Validation errors:', errors);

            // Display errors
            errors.forEach(error => {
                alert(error);
            });
        }
    });

}