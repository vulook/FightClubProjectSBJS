// Fetch roles from the server
let roleList = [];

document.addEventListener("DOMContentLoaded", async function fetchRoles() {
    try {
        const response = await fetch('/fight-club/admin/roles');
        roleList = await response.json();

    } catch (error) {
        console.error('Error fetching roles:', error);
    }
});

let isUser = true;

// Role verification
document.addEventListener("DOMContentLoaded", function verifyRole() {
    fetch('/fight-club/user')
        .then(response => response.json())
        .then(rolesJson => {
            isUser = !rolesJson.roles.some(role => role.roleName === "ADMIN");
            const userTab = document.getElementById("user-tab");
            const adminTab = document.getElementById("admin-tab");
            const userContent = document.getElementById("user");
            const adminContent = document.getElementById("admin");

            if (isUser) {
                userTab.classList.add("active");
                userContent.classList.add("show", "active");
                return getUser();
            } else {
                adminTab.classList.add("active");
                adminContent.classList.add("show", "active");
                return getAllUsers();
            }
        })
        .catch(error => {
            console.error('Error during role verification:', error);
        });
});

// Fill in the Header information
document.addEventListener("DOMContentLoaded", async function fillHeader() {
    try {
        const infoUser = document.querySelector('#info');

        // Fetch user information
        const userResponse = await fetch('/fight-club/user');
        const userJson = await userResponse.json();

        // Display user information and logo in the header
        const userRoles = userJson.roles.map(role => role.roleName).join(', ');
        const logoHtml = `<img src="/img/fight-club-emblem-new.jpg" alt="Fight Club Logo" class="logo img-fluid"> `;
        const userInfoHtml = `<span class="fw-bold">${userJson.username}</span><span class="spacer"> with roles </span><span>${userRoles}</span>`;

        // Combine logo and user information
        infoUser.innerHTML = `${logoHtml}${userInfoHtml}`;
    } catch (error) {
        console.error('Error during header information fetching:', error);
    }
});


