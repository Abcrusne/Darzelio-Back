(this["webpackJsonpdarzelis-react"]=this["webpackJsonpdarzelis-react"]||[]).push([[16],{440:function(e,t,a){"use strict";a.r(t),a.d(t,"default",(function(){return g}));var n=a(5),s=a(6),r=a(8),o=a(7),l=a(0),c=a.n(l),i=a(403),u=a(28),m=new(function(){function e(){Object(n.a)(this,e)}return Object(s.a)(e,[{key:"getAll",value:function(e,t,a){return u.a.get("/api/users/logs?page=".concat(e,"&sortby=").concat(a,"&email=").concat(t))}},{key:"sortByDate",value:function(e,t){return u.a.get(" /api/users/logs?page=".concat(e,"&sortby=").concat(t))}},{key:"sortByDateAsc",value:function(e){return u.a.get(" /api/users/logs?page=".concat(e,"&sortby=dateasc"))}},{key:"sortByDateDesc",value:function(e){return u.a.get(" /api/users/logs?page=".concat(e,"&sortby=datedesc"))}}]),e}()),g=(a(20),a(19),function(e){Object(r.a)(a,e);var t=Object(o.a)(a);function a(e){var s;return Object(n.a)(this,a),(s=t.call(this,e)).componentDidMount=function(){s.retrieveUsersLogsList()},s.retrieveUsersLogsList=function(){var e=s.state,t=e.pageNumber,a=e.searchEmail,n=e.sortByDate;m.getAll(t,a,n).then((function(e){s.setState({logs:e.data.logs,totalPages:e.data.totalPages,totalLogs:e.data.totalLogs})})).catch((function(e){}))},s.onChange=function(e){var t=e.target.value;s.setState({searchEmail:t},(function(){s.retrieveUsersLogsList()}))},s.handlePageChange=function(e,t){s.setState({pageNumber:t},(function(){s.retrieveUsersLogsList()}))},s.handleSortByDate=function(e){e.preventDefault(),"datedesc"===s.state.sortByDate?s.setState({sortByDate:"dateasc"},(function(){s.retrieveUsersLogsList()})):s.setState({sortByDate:"datedesc"},(function(){s.retrieveUsersLogsList()}))},s.state={logs:[],pageNumber:1,totalPages:0,totalLogs:0,searchEmail:"",sortByDate:"datedesc"},s}return Object(s.a)(a,[{key:"render",value:function(){var e=20*this.state.pageNumber-19;return c.a.createElement("div",{className:"container mt-5"},c.a.createElement("div",{className:"mb-4"},c.a.createElement("h4",null," Vartotoj\u0173 \u012fvyki\u0173 \u017eurnalas")),c.a.createElement("div",{className:"mb-4"},c.a.createElement("input",{type:"text",className:"form-control mt-3 col-4",placeholder:"Ie\u0161koti pagal vartotojo el.pa\u0161t\u0105",name:"searchEmail",value:this.state.searchEmail,onChange:this.onChange})),c.a.createElement("table",{className:"table mt-4"},c.a.createElement("thead",null,c.a.createElement("tr",null,c.a.createElement("th",{scope:"col"},"#"),c.a.createElement("th",{scope:"col"},"Vartotojas"),c.a.createElement("th",{scope:"col"},"Rol\u0117"),c.a.createElement("th",{scope:"col"},"Veiksmas"),c.a.createElement("th",{scope:"col",onClick:this.handleSortByDate,className:"dateTime"},"Data ir laikas"))),c.a.createElement("tbody",null,this.state.logs&&this.state.logs.length>0?this.state.logs.map((function(t,a){var n=t.id,s=t.user,r=t.role,o=t.action,l=t.date;return c.a.createElement("tr",{key:n},c.a.createElement("th",{scope:"row"},e++),c.a.createElement("td",null,s),c.a.createElement("td",null,r),c.a.createElement("td",null,o),c.a.createElement("td",null,l))})):c.a.createElement("tr",null,c.a.createElement("td",null,"...")))),c.a.createElement(i.a,{className:"my-3",count:this.state.totalPages,page:this.state.pageNumber,siblingCount:1,boundaryCount:1,variant:"outlined",shape:"rounded",onChange:this.handlePageChange}))}}]),a}(l.Component))}}]);
//# sourceMappingURL=16.fedbb4d2.chunk.js.map