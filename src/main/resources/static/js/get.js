// Function to fetch and display information about all users with the ADMIN role
async function getAllUsers() {
    try {
        const response = await fetch('/fight-club/admin');
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const table = document.querySelector("#tableUsers tbody");
        const users = await response.json();

        let html = "";
        users.forEach(user => {
            // Creating a row for each user
            html += `
                <tr class="table-light align-middle table-striped" style="height: 50px">
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.birthDate}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(role => " " + role.roleName)}</td>
                    <td>
                        <button class="btn btn-info" data-userid="${user.id}" data-action="edit" id="getEditModal" type="button" value="edit">edit</button>
                    </td>
                    <td>
                        <button class="btn btn-info"  data-userid="${user.id}" data-action="delete" id="getDeleteModal" type="button" value="delete">delete</button>
                    </td>
                </tr>
            `;
        });

        // Inserting created rows into the table
        table.innerHTML = html;

        // Adding event listeners to modal buttons
        document.querySelectorAll('#tableUsers > tbody > tr > td > button').forEach(button => {
            button.addEventListener('click', function (evt) {
                // Creating a modal window
                const myModal = new bootstrap.Modal(document.getElementById('modal'), {
                    keyboard: true,
                    backdrop: true,
                });

                let targetButton = $(evt.target);
                let buttonUserId = targetButton.attr('data-userid');
                let buttonAction = targetButton.attr('data-action');

                // Setting attributes for the modal window
                $('#modal').attr('data-userid', buttonUserId);
                $('#modal').attr('data-action', buttonAction);

                // Showing the modal window
                myModal.show();
            });
        });
    } catch (error) {
        console.error('Error during fetching all users:', error);
        alert('Error fetching all users. See console for details.');
    }
}

// Function to fetch and display information about the user with the USER role
async function getUser() {
    try {
        const response = await fetch('/fight-club/user');
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const table = document.querySelector("#tableUser tbody");
        const json = await response.json();

        // Creating a row for the user
        // Inserting the created row into the table
        table.innerHTML = `
            <tr class="table-light align-middle" style="height: 50px">
                <td>${json.id}</td>
                <td>${json.username}</td>
                <td>${json.birthDate}</td>
                <td>${json.email}</td>
                <td>${json.roles.map(role => " " + role.roleName)}</td>
            </tr>
        `;
    } catch (error) {
        console.error('Error during fetching user information:', error);
        alert('Error fetching user information. See console for details.');
    }
}

// Event handler for the click event on the "Admin" tab
document.querySelector('#admin-tab').addEventListener("click", async function () {
    document.getElementById("admin-tab").classList.add("active");
    document.getElementById("admin").classList.add("show", "active");

    // Wait for getAllUsers to complete before moving on
    await getAllUsers();
});

// Event handler for the click event on the "User" tab
document.querySelector('#user-tab').addEventListener('click', async function () {
    document.getElementById("user-tab").classList.add("active");
    document.getElementById("user").classList.add("show", "active");

    // Wait for getUser to complete before moving on
    await getUser();
});

// Event listener for modal show event
document.querySelector('#modal').addEventListener("show.bs.modal", async function (evt) {
    let thisModal = $(evt.target);
    let userid = thisModal.attr('data-userid');
    let action = thisModal.attr('data-action');

    if (action === 'edit') {
        await editUser(userid, thisModal);
    } else if (action === 'delete') {
        await deleteUser(userid, thisModal);
    }
});

// Event listener for modal hidden event
document.querySelector('#modal').addEventListener("hidden.bs.modal", function () {
    let thisModal = $(evt.target);
    thisModal.attr('data-action', '');
    thisModal.attr('data-userid', '');
    thisModal.find('.modal-title').html(``);
    thisModal.find('.modal-body').html(``);
    thisModal.find('.modal-footer').html(``);
});

// Event listener for modal hidePrevented event
document.querySelector('#modal').addEventListener("hidePrevented.bs.modal", function () {
    let thisModal = $(evt.target);
    thisModal.attr('data-action', '');
    thisModal.attr('data-userid', '');
    thisModal.find('.modal-title').html(``);
    thisModal.find('.modal-body').html(``);
    thisModal.find('.modal-footer').html(``);
});

