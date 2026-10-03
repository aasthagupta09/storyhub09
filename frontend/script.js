let stories=[
{id:1,title:"The Last Letter",author:"Aarav",category:"Romance",content:"A letter written ten years ago finally reaches its destination. What happens next changes two lives forever.",rating:4},
{id:2,title:"Midnight Mystery",author:"Riya",category:"Mystery",content:"Every night at midnight, the light in Room 13 turns on. One student decides to discover the truth.",rating:5},
{id:3,title:"The Haunted Hostel",author:"Dev",category:"Horror",content:"Three college friends enter an unused hostel room during a rainy night and discover something unexpected.",rating:4},
{id:4,title:"The Hidden Kingdom",author:"Kabir",category:"Fantasy",content:"Behind an ordinary forest is a magical kingdom that appears only under the moonlight.",rating:5},
{id:5,title:"Mountain Road",author:"Ishaan",category:"Adventure",content:"A group of friends takes an unknown mountain road and discovers a village missing from every map.",rating:4},
{id:6,title:"Wrong Delivery",author:"Anaya",category:"Comedy",content:"A food delivery reaches the wrong apartment and starts a chain of hilarious misunderstandings.",rating:3}
];

let favourites=JSON.parse(localStorage.getItem("storyhubFavourites"))||[];

function createCard(story){
 const saved=favourites.includes(story.id);
 return `<div class="story-card">
 <div class="icon">📖</div><span class="category">${story.category}</span>
 <h3>${escapeHTML(story.title)}</h3><span class="author">by ${escapeHTML(story.author)}</span>
 <p>${escapeHTML(story.content.substring(0,90))}...</p><p>⭐ ${story.rating||0}/5</p>
 <div class="card-buttons">
 <button class="small-btn" onclick="readStory(${story.id})">Read</button>
 <button class="small-btn" onclick="toggleFavourite(${story.id})">${saved?"❤️ Saved":"♡ Favourite"}</button>
 </div></div>`;
}

function displayStories(){
 const list=document.getElementById("storyList");
 const search=document.getElementById("search").value.toLowerCase();
 const category=document.getElementById("category").value;
 const filtered=stories.filter(s=>
  (s.title.toLowerCase().includes(search)||s.author.toLowerCase().includes(search)) &&
  (category==="All"||s.category===category)
 );
 list.innerHTML=filtered.length?filtered.map(createCard).join(""):"<p>No stories found.</p>";
}

function displayFavourites(){
 const list=document.getElementById("favouriteList");
 const result=stories.filter(s=>favourites.includes(s.id));
 list.innerHTML=result.length?result.map(createCard).join(""):"<p>No favourite stories yet. Add some ❤️</p>";
}

function toggleFavourite(id){
 if(favourites.includes(id)) favourites=favourites.filter(x=>x!==id);
 else favourites.push(id);
 localStorage.setItem("storyhubFavourites",JSON.stringify(favourites));
 displayStories();displayFavourites();
}

function readStory(id){
 const story=stories.find(s=>s.id===id);
 document.getElementById("readerTitle").innerText=story.title;
 document.getElementById("readerAuthor").innerText="By "+story.author;
 document.getElementById("readerCategory").innerText="Category: "+story.category;
 document.getElementById("readerContent").innerText=story.content;
 let buttons="";
 for(let i=1;i<=5;i++) buttons+=`<button onclick="rateStory(${story.id},${i})">${i<=story.rating?"★":"☆"}</button>`;
 document.getElementById("ratingButtons").innerHTML=buttons;
 document.getElementById("reader").classList.remove("hidden");
}

function closeReader(){document.getElementById("reader").classList.add("hidden")}

function rateStory(id,rating){
 const story=stories.find(s=>s.id===id);
 story.rating=rating;
 alert("Rating saved: "+rating+"/5");
 readStory(id);displayStories();displayFavourites();
}

document.getElementById("search").addEventListener("input",displayStories);
document.getElementById("category").addEventListener("change",displayStories);

document.getElementById("storyForm").addEventListener("submit",function(e){
 e.preventDefault();
 stories.push({
  id:Date.now(),
  title:document.getElementById("title").value,
  author:document.getElementById("author").value,
  category:document.getElementById("storyCategory").value,
  content:document.getElementById("content").value,
  rating:0
 });
 alert("Story published successfully! 🎉");
 this.reset();displayStories();displayFavourites();
 document.getElementById("stories").scrollIntoView();
});

function escapeHTML(text){
 return String(text).replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#039;"}[m]));
}

displayStories();
displayFavourites();
