<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.1.0/css/all.css">
    <title>Hello, world!</title>
</head>
<body>

<main role="main">
    <section class="jumbotron text-center">
        <div class="container">
            <h1><img src="https://spring.io/images/OG-Spring.png"></h1>
            <h3>Hellooooooo NEW CICD</h3>
            <h3>${host}</h3>
            <p>${date}</p>
            <p>${message}</p>
            <p>${version}</p>
            <p>
                <button class="btn btn-primary btn-thumbs-up"><i class="far fa-thumbs-up"></i> <span id="thumbs-up-count">0</span></button>
                <button class="btn btn-danger btn-thumbs-down"><i class="far fa-thumbs-down"></i> <span id="thumbs-down-count">0</span></button>
            </p>
        </div>
    </section>
</main>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.2/js/bootstrap.min.js"></script>
</body>
</html>
