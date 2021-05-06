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
        addToFavoritesAjax(productId);
    });

    $("#btnBasket").click(function () {
        addToBasketAjax(productId);
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
                url: "/product/subscribe",
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({"email": email, "productId": productId}),
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
                url: "/product/review",
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify({"text": review, "productId": productId}),
                dataType: "json",
                headers: {"Authorization": 'Bearer ' + sessionStorage.getItem('token')},
                beforeSend: function () {
                    showSpinner("btnReviewSubmission");
                },
                success: function (data) {
                    if (data.message === "success") {
                        location.reload();
                    } else {
                        responseDiv.text(sf);
                        responseDiv.removeClass("alert-success");
                        responseDiv.addClass("alert-danger");
                        responseDiv.css("display", "block");
                    }
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

function addToBasketAjax(productId) {
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
}

function addToFavoritesAjax(productId) {
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
}

