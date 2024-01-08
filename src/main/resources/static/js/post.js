// Event listener for adding a new user
document.querySelector('#addNewUser').addEventListener("click", async function addUser(evt){
    try {
        evt.preventDefault();

        // Get values from the form
        let addForm = document.querySelector('#formAddUser');
        let username = addForm.addUsername.value;
        let birthDate = addForm.addBirthDate.value;
        let email = addForm.addEmail.value;
        let password = addForm.addPassword.value;

        // Function to get selected roles from the dropdown
        let getRole = () => {
            let array = [];
            let role = document.querySelector('#Inputrole');
            for (let i = 0; i < role.length; i++) {
                if (role[i].selected) {
                    array.push(roleList[i]);
                }
            }
            return array;
        };

        // User object with form data
        let user = {
            username: username,
            birthDate: birthDate,
            email: email,
            password: password,
            roles: getRole()
        };

        // Send a POST request to add a new user
        let addUser = await fetch('/fight-club/admin', {
            method: "POST",
            headers: {
                'Content-type': 'application/json',
                'Accept': 'application/json',
            },
            body: JSON.stringify(user)
        });

        if (addUser.ok) {
            // If successful, update the user's table
            await getAllUsers();

            // Clear form fields after adding a new user
            let addForm = $('#formAddUser');
            addForm.find('#username').val('');
            addForm.find('#email').val('');
            addForm.find('#birthDate').val('');
            addForm.find('#password').val('');
            addForm.find(getRole()).val('');

            alert('New user successfully added!');

        } else {
            // Handle validation errors
            let errors = await addUser.json();
            console.error('Failed to add a new user. Validation errors:', errors);

            // Display errors
            errors.forEach(error => {
                alert(error);
            });
        }

    } catch (error) {
        console.error('Error during adding a new user:', error);
        alert('Error adding a new user. See console for details.');
    }
});