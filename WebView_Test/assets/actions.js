var memory = 0,
    calc = 0,
	old_calc = calc,
    input = false,
	This = window;

function inArray(val, arr) {
    for (i = 0; i < arr.length; i++) {
        if (val == arr[i]) {
            return true;
        }
    }

    return false;
}

function check(r) {
    if (r === true) {
        $("#screen").css({
            "color": "#333",
            "font-size": "32px",
            "bottom": "10px"
        });
    }
	
    var Max = 20;
    var size = eval($("#screen").css("font-size").split("px"));
    size = size[0] + size[1];
    var bottom = eval($("#screen").css("bottom").split("px"));
    bottom = bottom[0] + bottom[1];

    if (size <= Max) {
        $("#screen").css({
            "font-size": "20px",
            "bottom": "0px"
        });

        $("#screen").html($("#screen").html().substring(0, 21));
		
		var div = $("#screen").html();
		
		if (div.length == 21) {
			$("#screen").css({
				"color": "#C03"
			});
		}
		
        return;
    }
	
	if ($("#screen").width() > $(".screen").width() - 20) {
		$("#screen").css({
			"font-size": size - 2,
			"bottom": bottom - 2
		});
		
		check();
	}
		
	bottom = eval($("#screen").css("bottom").split("px"));
	bottom = bottom[0] + bottom[1];
	
	if (bottom < 0) {
		$("#screen").css({
			"bottom": 0
		});
	}
}

function enter(x, ns) {
    if (ns !== true) {
        calc += $("#screen").html();
    }

    calc += x;

    $("#method").html("");

    for (var i = 0; i <= calc.length; i++) {
        switch (calc[i]) {
        case "+":
            $("#method").append("<span style=\"margin-left: 5px;margin-right: 5px;\">&#43;</span>");
            break;
        case "-":
            $("#method").append("<span style=\"margin-left: 5px;margin-right: 5px;\">&#45</span>");
            break;
        case "*":
            $("#method").append("<span style=\"margin-left: 5px;margin-right: 5px;\">&times;</span>");
            break;
        case "/":
            $("#method").append("<span style=\"margin-left: 5px;margin-right: 5px;\">&divide;</span>");
            break;
        default:
            $("#method").append(calc[i]);
            break;
        }
    }

    $("#screen").html(0);
	check(true);
}

function key(x, y) {
    switch (x) {
    case "ac":
        $("#screen").html(0);
        $("#method").html("");
        calc = 0;
		check(true);
        break;
    case "ce":
        $("#screen").html(0);
		check(true);
        break;
    case "=":
        calc += $("#screen").html();
		calc = eval(calc);
		old_calc = calc;
		
        $("#screen").html(calc);
        $("#method").html("");
		
		calc = 0;
        input = false;
		check();
        break;
    case "backspace":
		check(true);
		var temp = $("#screen").html().substring(0, $("#screen").text().length - 1);
		
        if (temp == "") {
            $("#screen").html(0);
        } else {
            $("#screen").html(temp);
			check();
        }
        break;
    case "esc":
        if (y === true || confirm("Are you sure you want to exit?")) {
            This.open("", "_self", "");
            This.close();
        }
        break;
    case "mp":
        memory = memory + eval($("#screen").html());
        break;
    case "mm":
        memory = memory - eval($("#screen").html());
        break;
    case "ms":
        memory = eval($("#screen").html());
        break;
    case "mr":
        $("#screen").html(memory);
		check();
        break;
    case "mc":
        memory = 0;
        break;
    default:
        if (calc == 0) {
            calc = "";
        }

        switch (x) {
        case "+":
            if (calc != "" && $("#screen").html() == "0") {
                calc = calc.slice(0, -1);
                enter(x, true);
                $("#screen").html(Screen);
            } else {
                enter(x);
            }
            break;
        case "-":
            if (calc != "" && $("#screen").html() == "0") {
                calc = calc.slice(0, -1);
                enter(x, true);
                $("#screen").html(Screen);
            } else {
                enter(x);
            }
            break;
        case "*":
            if (calc != "" && $("#screen").html() == "0") {
                calc = calc.slice(0, -1);
                enter(x, true);
                $("#screen").html(Screen);
            } else {
                enter(x);
            }
            break;
        case "/":
            if (calc != "" && $("#screen").html() == "0") {
                calc = calc.slice(0, -1);
                enter(x, true);
                $("#screen").html(Screen);
            } else {
                enter(x);
            }
            break;
		case ".":
			var Screen = $("#screen").html();
			
			for (var i = 0; i < Screen.length; i++) {
				if (Screen[i] == x) {
					return false;
				}
			}
				
			if ($("#screen").html() == "0") {
				$("#screen").html("0" + x);
				check(true);
			} else {
				$("#screen").append(x);
				check();
			}
			break;
        default:
            if ($("#screen").html() == "0" && $("#screen").html() != "0.") {
                $("#screen").html(x);
				check(true);
            } else {
                $("#screen").append(x);
				check();
            }
            break;
        }
    }
}

function popup() {
	localStorage.mode = "popup";
	
    window.open("index.html", Math.random(), "toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, height=405px, width=274px, left=0, top=0, ScreenX=" + localStorage.popupX + ", ScreenY=" + localStorage.popupY);
	
    key("esc", true);
}

function browser() {
	localStorage.mode = "browser";
    This.open("index.html").focus();
    key("esc", true);
}

$(window).resize(function(e) {
    if (localStorage.displayIn == "browser") {
        $(".settings").css({
            "left": ($(document).width() / 2) - 250,
        });
    }
})

if (localStorage.displayIn == "popup") {
	var oldX = window.screenX,
    	oldY = window.screenY;

	var interval = setInterval(function(){
		if (oldX != window.screenX || oldY != window.screenY) {
			localStorage.popupX = window.screenX;
			localStorage.popupY = window.screenY;
		}
		
		oldX = window.screenX;
		oldY = window.screenY;
	}, 500);
}

$(document).ready(function() {
    $(".calculator").hide().fadeIn(250);
	
	if (!localStorage.popupX) {
		localStorage.popupX = ($(window).width() / 2) - 274;
	}
	
	if (!localStorage.popupY) {
		localStorage.popupY = ($(window).height() / 2) - 405;
	}
	
    if (!localStorage.displayIn) {
        localStorage.displayIn = "browser";
    }

    if (localStorage.displayIn == "browser") {
        $(".settings").css({
            "position": "fixed",
            "top": 100,
            "width": 500,
            "left": ($(document).width() / 2) - 250,
        });
    }

    if (!localStorage.mode) {
        localStorage.mode = "browser";
    }

    if (localStorage.displayIn == "popup" && localStorage.mode != "popup") {
        $(".settings .displayin .selected").removeClass("selected");
        $(".settings .displayin .popup").addClass("selected");
		
		popup();
    } else if (localStorage.mode == "popup") {
        $(".settings .displayin .selected").removeClass("selected");
        $(".settings .displayin .popup").addClass("selected");

        $("body").addClass("popup");
		
		localStorage.mode = "browser";
    } else {
        $(".settings .displayin .selected").removeClass("selected");
        $(".settings .displayin .browser").addClass("selected");
    }

    if (!localStorage.theme) {
        localStorage.theme = "light";
    }
	
    temp = (localStorage.theme == "light") ? "dark" : "light";
    $("body").removeClass(temp);
    temp = (localStorage.theme == "light") ? "light" : "dark";
    $("body").addClass(temp);
    $("." + temp + "Button").addClass("selected");

    $(".lightButton, .darkButton").click(function() {
        var button = $(this).attr("class").split("Button");
        button = button[0] + button[1];

        if ($("body").hasClass(button)) {
            return false;
        }

        $(".settings .theme .selected").removeClass("selected");
        $(this).addClass("selected");

        $(".calculator, .settings").fadeOut(250);

        if ($(this).hasClass("lightButton")) {
            localStorage.theme = "light";
            $("body").switchClass("dark", "light", 250);
        } else {
            localStorage.theme = "dark";
            $("body").switchClass("light", "dark", 250);
        }

        if ($(".settings").css("display") != "block") {
            $(".calculator").fadeIn(250);
        } else {
            $(".settings").fadeIn(250);
        }
    });

    $(".settingsButton").click(function() {
        if (localStorage.displayIn == "popup") {
            window.resizeBy(0, -285);
            $(".calculator, .settingsButton").hide();
            $(".settings, .back").show();
        } else {
            $(".calculator, .settingsButton").fadeOut(1000);
            $(".settings, .back").fadeIn(1000);
        }
    });

    $(".back").click(function() {
        if (localStorage.displayIn == "popup") {
            window.resizeBy(0, 285);
            $(".calculator, .settingsButton").show();
            $(".settings, .back").hide();
        } else {
            $(".calculator, .settingsButton").fadeIn(1000);
            $(".settings, .back").fadeOut(1000);
        }
    });

    $(".settings .browser").click(function() {
        $(".settings .displayin .selected").removeClass("selected");
        $(this).addClass("selected");

        if (localStorage.displayIn == "browser") {
            return false;
        }

        localStorage.displayIn = "browser";

        browser();
    });

    $(".settings .popup").click(function() {
        $(".settings .displayin .selected").removeClass("selected");
        $(this).addClass("selected");

        if (localStorage.displayIn == "popup") {
            return false;
        }

        localStorage.displayIn = "popup";

        popup();
    });
});

$(document).keyup(function(evt) {
    if (!evt) var evt = window.event;
    evt.preventDefault();
});

$(document).keypress(function(evt) {
    if (!evt) var evt = window.event;
    evt.preventDefault();
});

$(document).keydown(function(evt) {
    if (!evt) var evt = window.event;
    var keyCode = evt.keyCode ? evt.keyCode : evt.which ? evt.which : evt.charCode;
    var x = -1;
    var y = -1;
	
	evt.preventDefault();

    if (keyCode == 8) {
        key("backspace");
        return false;
    }

    if (keyCode == 27) {
        key("esc");
        return false;
    }

    if (keyCode == 36) {
        key("ac");
        return false;
    }

    if (keyCode == 46) {
        key("ce");
        return false;
    }

    if (keyCode == 111 || keyCode == 191) {
        key("/");
        return false;
    }

    if (keyCode == 110 || keyCode == 190) {
        key(".");
        return false;
    }

    if (keyCode == 109 || keyCode == 189) {
        key("-");
        return false;
    }

    if (keyCode == 13 || (!evt.shiftKey && keyCode == 187)) {
        key("=");
        return false;
    }

    if ((evt.shiftKey && keyCode == 187) || keyCode == 107) {
        key("+");
        return false;
    }

    if ((evt.shiftKey && keyCode == 56) || keyCode == 106 || keyCode == 88) {
        key("*");
        return false;
    }

    for (i = 96; i < 106; i++) {
        x++;
        if (keyCode == i) {
            key(x);
            return false;
        }
    }

    for (i = 48; i < 59; i++) {
        y++;
        if (keyCode == i) {
            key(y);
            return false;
        }
    }

    return false;
});