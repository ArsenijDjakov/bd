$(function () {
    if (checkLogin() === true) {
        $('#btnFavorites').show();
        $('#btnBasket').show();
        $('#reviewBlock').show();
    } else {
        $('#btnFavorites').hide();
        $('#btnBasket').hide();
        $('#reviewBlock').hide();
    }
    $("#btnFavorites").click(function () {
        var responseDiv = $("#userSubmissionResult");
        responseDiv.css("display", "none");
        $.ajax({
                url: "/account/favorites/" + productId,
                type: 'POST',
                headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
                success: function (data) {
                    if (data.message === "success") {
                        responseDiv.text(pss);
                        responseDiv.removeClass("alert-danger");
                        responseDiv.addClass("alert-success");
                    } else if (data.message === "alreadyInFavorites") {
                        responseDiv.text(aif);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    } else {
                        responseDiv.text(sf);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    }
                    responseDiv.css("display", "block");
                },
                error: function () {
                    responseDiv.text(sf);
                    responseDiv.removeClass("alert-success");
                    responseDiv.addClass("alert-danger");
                    responseDiv.css("display", "block");
                }
            }
        );
    });

    $("#btnBasket").click(function () {
        var responseDiv = $("#userSubmissionResult");
        responseDiv.css("display", "none");
        $.ajax({
                url: "/account/basket/" + productId,
                type: 'POST',
                headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
                success: function (data) {
                    if (data.message === "success") {
                        responseDiv.text(pss);
                        responseDiv.removeClass("alert-danger");
                        responseDiv.addClass("alert-success");
                    } else if (data.message === "alreadyInBasket") {
                        responseDiv.text(aib);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    } else {
                        responseDiv.text(sf);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    }
                    responseDiv.css("display", "block");
                },
                error: function () {
                    responseDiv.text(sf);
                    responseDiv.removeClass("alert-success");
                    responseDiv.addClass("alert-danger");
                    responseDiv.css("display", "block");
                }
            }
        );
    });

    $("#emailSubmission").click(function () {
        var email = $("#emailAddress").val();
        var responseDiv = $("#emailSubmissionResult");
        responseDiv.css("display", "none");
        const regex = /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;
        if (!regex.test(email)) {
            responseDiv.text(evf);
            responseDiv.removeClass("alert-success");
            responseDiv.addClass("alert-danger");
            responseDiv.css("display", "block");
            return;
        }
        $.ajax({
                url: productId + "/subscribe",
                type: 'POST',
                data: JSON.stringify({"email": email}),
                headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
                success: function (data) {
                    if (data.message === "success") {
                        $("#emailAddress").val('');
                        responseDiv.text(ess);
                        responseDiv.removeClass("alert-danger");
                        responseDiv.addClass("alert-success");
                    } else if (data.message === "alreadySub") {
                        responseDiv.text(esa);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    } else {
                        responseDiv.text(sf);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                    }
                    responseDiv.css("display", "block");
                },
                error: function () {
                    responseDiv.text(sf);
                    responseDiv.removeClass("alert-success");
                    responseDiv.addClass("alert-danger");
                    responseDiv.css("display", "block");
                }
            }
        );
    });

    $("#btnReviewSubmission").click(function () {
        var responseDiv = $("#reviewSubmissionResult");
        var review = $("#review").val();
        if (review.length === 0) {
            responseDiv.text(re);
            responseDiv.removeClass("alert-success");
            responseDiv.addClass("alert-danger");
            responseDiv.css("display", "block");
            return;
        }
        responseDiv.css("display", "none");
        $.ajax({
                url: "/product/review/add",
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({"text": review, "productId": productId}),
                dataType: "json",
                headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
                beforeSend: function () {
                    showSpinner("btnReviewSubmission");
                },
                success: function () {
                    location.reload();
                },
                error: function () {
                    hideSpinner("btnReviewSubmission");
                    responseDiv.text(sf);
                    responseDiv.removeClass("alert-success");
                    responseDiv.addClass("alert-danger");
                    responseDiv.css("display", "block");
                }
            }
        );
    });
});
