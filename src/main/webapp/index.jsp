<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html>
<html lang="en">

<head>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <title>Cinema</title>
</head>
<body>
<!-- Optional JavaScript -->
<script>
    $(document).ready(function() {
        checkSeats();
        setInterval(checkSeats, 30000);
    });

    function checkSeats() {
        $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/cinema/hall.do',
            contentType: 'application/json',
            dataType: 'json'
        }).done(function (response) {
            let rowsNum = response.rows;
            let colsNum = response.cols;
            let seats = response.seats;
            let content = '<thead><tr><th style="width: 120px;">Ряд / Место</th>';
            for (let i = 0; i < colsNum; i++) {
                content += '<th>' + (i + 1) + '</th>'
            }
            let i = 0;
            content += '</tr></thead><tbody>';
            for (let curRow = 0; curRow < rowsNum; curRow++) {
                content += '<tr><th>' + (curRow + 1) + '</th>';
                for (let curCol = 0; curCol < colsNum; curCol++) {
                    let tdContent = "";
                    let seatText = "Ряд " + (curRow + 1) + ", Место " + (curCol + 1);
                    if (seats[i].available === "false") {
                        tdContent = "<label>" + seatText + "&#9747;</label>";
                    } else {
                        tdContent = "<label>" + seatText
                            + "<input type=\"checkbox\" name=\"seat" + seats[i].id
                            + "\" value=\"" + seats[i].id + "\"" + "></label>";
                    }
                    content += '<td>' + tdContent + '</td>';
                    i++;
                }
                content += '</tr>';
            }
            content += '</tbody>';
            $('#main_table').html(content);
        }).fail(function (err) {
            alert(err);
        });
    }
</script>

<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<div class="container">
    <h4>
        Бронирование мест на сеанс
    </h4>
    <div class="row pt-3">
        <form action="<%=request.getContextPath()%>/hall.do" method="post">
            <table class="table table-bordered" id="main_table">
            </table>
            <button type="submit" class="btn btn-primary">Оплатить</button>
        </form>
    </div>
</div>
</body>
</html>