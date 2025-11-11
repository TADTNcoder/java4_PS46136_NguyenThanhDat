<%@ page contentType="text/css" %>
<style>
body {
    background: #f8f9fa;
    font-family: Arial, sans-serif;
    margin-top: 80px;
}
.dashboard-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 2rem;
}
.welcome {
    font-size: 2rem;
    font-weight: bold;
    margin-bottom: 1rem;
}
.role-badge {
    display: inline-block;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.9rem;
    color: #fff;
}
.role-admin { background: #dc3545; }
.role-editor { background: #0d6efd; }
.role-subscriber { background: #198754; }
.card-custom {
    border: none;
    border-radius: 15px;
    box-shadow: 0 4px 15px rgba(0,0,0,0.08);
    transition: transform 0.2s;
}
.card-custom:hover { transform: translateY(-5px); }
.card-custom .card-body {
    padding: 1.8rem;
    text-align: center;
}
.card-custom h5 { font-weight: 600; }
.card-custom p { color: #6c757d; }
.logout-btn { margin-top: 2rem; }
.divider {
    margin: 2rem 0;
    border-top: 2px solid #dee2e6;
}
.list-group-item a {
    text-decoration: none;
    color: #0d6efd;
}
.list-group-item a:hover {
    text-decoration: underline;
    color: #084298;
}
</style>
