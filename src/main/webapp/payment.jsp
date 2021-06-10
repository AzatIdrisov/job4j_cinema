<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="ru.job4j.cinema.store.PsqlHallStore" %>
<%@ page import="ru.job4j.cinema.model.Seat" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">

    <title>Страница оплаты</title>
</head>
<body>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>

<div class="container">
    <div class="row pt-3">
        <table class="table table-condensed">
            <thead>
            <tr>
                <th>Вы выбрали места:</th>
            </tr>
            </thead>
            <tbody>
            <%
                List<Integer> reservedId = (List<Integer>) request.getAttribute("reserved");
            %>
            <% for (Integer integer : reservedId) { %>
            <%
                int id = integer;
                Seat seat = PsqlHallStore.instOf().getSeatById(id);
                String text = "Ряд " + seat.getRow() + ", Место " + seat.getCol() + ". Стоимость: 500р.";
            %>
            <tr>
                <td><%=text%>
                </td>
            </tr>
            <% } %>
            <tr><td>Сумма: <%=reservedId.size() * 500%> рублей.</td></tr>
            </tbody>
        </table>
    </div>
    <div class="row">
        <%
            String reserved = reservedId.stream().map(i -> ""+i).collect(Collectors.joining("x"));
            System.out.println(reserved);
        %>
        <form action="<%=request.getContextPath()%>/payment.do?reserved=<%=reserved%>" method="post">
            <div class="form-group">
                <label for="username">ФИО</label>
                <input type="text" class="form-control" name="username" id="username" placeholder="ФИО">
            </div>
            <div class="form-group">
                <label for="phone">Номер телефона</label>
                <input type="text" class="form-control" name="phone" id="phone" placeholder="Номер телефона">
            </div>
            <div class="form-group">
                <label for="email">email</label>
                <input type="text" class="form-control" name="email" id="email" placeholder="email">
            </div>
            <button type="submit" class="btn btn-primary">Оплатить</button>
        </form>
    </div>
</div>
</body>
</html>