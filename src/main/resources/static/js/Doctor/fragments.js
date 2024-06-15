function logout() {
    window.location.href = "/logout";
}
document.getElementById('hamburger').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('show');
});
document.getElementById('hamburger').addEventListener('click', function() {
    document.getElementById('sidebar').classList.toggle('show');
});

document.querySelectorAll('.sidebar a').forEach(item => {
    item.addEventListener('click', function() {
        document.getElementById('sidebar').classList.remove('show');
    });
});
