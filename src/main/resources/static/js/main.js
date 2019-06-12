var base = contextRoot + "fileBox/api";

var http = new XMLHttpRequest();

function updateVisibility(id, index) {
  var update = document.getElementById("visibility" + index);
  
  var url = base + "/files/access";
  var data = {
    fileVisibility: update.options[update.selectedIndex].value,
    id: id
  };
  
  http.open("POST", url);
  http.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  http.send(JSON.stringify(data))
}

http.onreadystatechange = function() {
  if (this.readyState !== 4) {
    return
  }
  
  alert(this.responseText)
};