import api from "/common/js/API.js"

async function renderContent() {
  const data = (await api.get(`counsels/detail`, {
    params: {id: location.pathname.split("/").pop()}
  })).data
  document.getElementById("title").textContent = data.title;
  document.getElementById("content").textContent = data.content;
  document.getElementById("name").textContent = data.email;
  document.getElementById("time").textContent = data.createdDate;
  document.getElementById("category").textContent = data.category;

  console.log(data);
}

async function renderComment() {
  const data = (await api.get(`comments`, {
    params: {counselId: location.pathname.split("/").pop()}
  })).data
  console.log(data);
  const commentList = document.getElementById("comment-list");
  commentList.innerHTML = "";
  const commentData = data.content;
  commentData.forEach(item => {

    const commentDiv = document.createElement("div");
    commentDiv.className = "comment";

    commentDiv.innerHTML = `
      <div class="comment">
        <div class="comment-meta">${item.email} | ${item.createdDate}</div>
        <div class="comment-item">${item.content}</div>
      </div>
    `

    commentList.appendChild(commentDiv);
  })
}

renderContent();
renderComment();

const commentBtn = document.getElementById("comment-btn");
const deleteBtn = document.getElementById("delete");
const updateBtn = document.getElementById("update");

if(commentBtn !== null){
  commentBtn.addEventListener("click", () => {
    commentBtn.style.display = "none";
    document.getElementById("comment-content").style.display = "block";
    document.getElementById("confirm-btn").style.display = "block";
  });
}

if(deleteBtn !== null){
  deleteBtn.addEventListener("click", () => {
    api.delete("counsels", {
      params: {
        id: location.pathname.split("/").pop()
      }
    })
      .then(response => {
        location.href = response.headers.location;
      })
  })
}

if(updateBtn !== null){
  updateBtn.addEventListener("click", () => {
    location.href = `/counsels/update/${location.pathname.split("/").pop()}`
  });
}

document.getElementById("confirm-btn").addEventListener("click", () => {
  api.post("comments",{
    counselId: location.pathname.split("/").pop(),
    content: document.getElementById("comment-content").value
  })
    .then(response => {
      location.href = location.pathname;
    })
});