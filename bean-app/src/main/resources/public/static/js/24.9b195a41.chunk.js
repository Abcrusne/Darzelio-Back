(this["webpackJsonpdarzelis-react"]=this["webpackJsonpdarzelis-react"]||[]).push([[24],{426:function(e,a,t){"use strict";t.r(a),t.d(a,"default",(function(){return P}));var n=t(12),r=t(5),s=t(6),o=t(8),l=t(7),c=t(0),m=t.n(c),i=t(2),d=t(1),u=t.n(d),h=(t(19),t(88),t(46)),b=t.n(h),g=t(58),p=t.n(g);Object(h.registerLocale)("lt",p.a),u.a.defaults.withCredentials=!0;var P=function(e){Object(o.a)(t,e);var a=Object(l.a)(t);function t(e){var s;return Object(r.a)(this,t),(s=a.call(this,e)).handleChangeDate=function(e){s.setState({birthdate:e})},s.handleChange=function(e){var a=RegExp(/^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i),t=e.target,r=t.name,o=t.value,l=s.state.errors,c=/^[A-Za-z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d ]+$/,m=/^[1-9][a-zA-Z 0-9 ]*$/,i=/^[a-zA-Z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d][ a-zA-Z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d0-9 ,.\- ]*$/;switch(r){case"firstname":l.firstname=!o.match(c)||o.length<2||0===o.length?"Vardas turi b\u016bti i\u0161 raid\u017ei\u0173 ir ilgesnis nei 1 raid\u0117! ":"";break;case"lastname":l.lastname=!o.match(c)||o.length<2||0===o.length?"Pavard\u0117 turi b\u016bti i\u0161 raid\u017ei\u0173 ir ilgesn\u0117 nei 1 raid\u0117! ":"";break;case"personalCode":l.personalCode=!o.match(/^[5|6]+[0-9]+$/)||o.length<11||o.length>11||0===o.length?"Vaiko asmens kodo formatas: 59001011111 arba 69001011111 ":"";break;case"birthdate":l.birthdate=o.match(/([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/)&&0!==o.length?"":"Gimimo datos formatas: 2020-01-01 ";break;case"street":l.street=o&&o.match(i)&&0!==o.length?"":"\u012era\u0161ykite gatv\u0119!";break;case"city":l.city=o&&o.match(c)&&0!==o.length?"":"\u012era\u0161ykite miest\u0105";break;case"houseNumber":l.houseNumber=o&&o.match(m)&&0!==o.length?"":"\u012era\u0161ykite namo numer\u012f, pvz.: 1A";break;case"secondParentFirstname":l.secondParentFirstname=!o.match(c)||o.length<2||0===o.length?"Vardas turi b\u016bti i\u0161 raid\u017ei\u0173 ir ilgesnis nei 1 raid\u0117! ":"";break;case"secondParentLastname":l.secondParentLastname=!o.match(c)||o.length<2||0===o.length?"Pavard\u0117 turi b\u016bti i\u0161 raid\u017ei\u0173 ir ilgesn\u0117 nei 1 raid\u0117! ":"";break;case"secondParentPersonalCode":l.secondParentPersonalCode=!o.match(/^[3|4|5|6]+[0-9]+$/)||o.length<11||o.length>11||0===o.length?"Asmens kodo formatas: 39001011111, 49001011111, 59001011111 arba 69001011111  ":"";break;case"secondParentStreet":l.secondParentStreet=o&&o.match(i)&&0!==o.length?"":"\u012era\u0161ykite gatv\u0119!";break;case"secondParentCity":l.secondParentCity=o&&o.match(c)&&0!==o.length?"":"\u012era\u0161ykite miest\u0105";break;case"secondParentHouseNumber":l.secondParentHouseNumber=o&&o.match(m)&&0!==o.length?"":"\u012era\u0161ykite namo numer\u012f, pvz.: 1A";break;case"secondParentEmail":l.secondParentEmail=a.test(o)||0===o.length?"":"El.pa\u0161tas netinkamas! Formato pvz.: vardas@mail.com";break;case"secondParentPhone":l.secondParentPhone=!o.match(/^[+][3][7][0][6|5]+[0-9]+$/)||o.length<12||o.length>12||0===o.length?"Telefono numerio formatas +37061234567 arba +37051234567":"";break;case"secondParentNumberOfKids":l.secondParentNumberOfKids=!o.match(/^[0-9]+$/)||o.length<0?"\u012era\u0161ykite vaik\u0173 skai\u010di\u0173":""}"checkbox"===e.target.type?s.setState(Object(n.a)({},e.target.name,e.target.checked)):s.setState(Object(n.a)({errors:l},e.target.name,e.target.value),(function(){}))},s.handleSubmit=function(e){e.preventDefault();var a={birthdate:s.state.birthdate,id:s.state.id,firstname:s.state.firstname,lastname:s.state.lastname,personalCode:s.state.personalCode,city:s.state.city,street:s.state.street,houseNumber:s.state.houseNumber,flatNumber:s.state.flatNumber,secondParentId:s.state.secondParentId,secondParent:s.state.secondParent,secondParentFirstname:s.state.secondParentFirstname,secondParentLastname:s.state.secondParentLastname,secondParentEmail:s.state.secondParentEmail,secondParentPhone:s.state.secondParentPhone,secondParentPersonalCode:s.state.secondParentPersonalCode,secondParentCity:s.state.secondParentCity,secondParentStreet:s.state.secondParentStreet,secondParentHouseNumber:s.state.secondParentHouseNumber,secondParentFlatNumber:s.state.secondParentFlatNumber,secondParentNumberOfKids:s.state.secondParentNumberOfKids,secondParentStudying:s.state.secondParentStudying,secondParentStudyingInstitution:s.state.secondParentStudyingInstitution,secondParentHasDisability:s.state.secondParentHasDisability,secondParentDeclaredResidenceSameAsLiving:s.state.secondParentDeclaredResidenceSameAsLiving,secondParentDeclaredCity:s.state.secondParentDeclaredCity,secondParentDeclaredStreet:s.state.secondParentDeclaredStreet,secondParentDeclaredHouseNumber:s.state.secondParentDeclaredHouseNumber,secondParentDeclaredFlatNumber:s.state.secondParentDeclaredFlatNumber,adopted:s.state.adopted};!function(e){var a=!0;return Object.values(e).forEach((function(e){return e.length>0&&(a=!1)})),a}(s.state.errors)?alert("Registracija nes\u0117kminga! Pasitikrinkite ar pa\u017eym\u0117jote bei u\u017epild\u0117te laukus teisingai. "):u.a.post("".concat(i.a,"/api/users/").concat(s.props.match.params.id,"/parentdetails/children"),a).then((function(e){alert("Vaiko duomen\u0173 registracija s\u0117kminga"),s.props.history.push("/admin/duomenys/vaikai/".concat(s.props.match.params.id))})).catch((function(e){"This personal code already exists"===e.response.data?alert("Pasitikrinkite ar suved\u0117te teisingus asmens kodus. Toks vaiko asmens kodas jau egzstuoja! "):"Item already exists"===e.response.data.message?alert("Pasitikrinkite ar suved\u0117te teisingus asmens kodus. Toks asmens kodas jau egzistuoja"):"Vaiko ir t\u0117vo asmens kodai negali sutapti"===e.response.data?alert("Pasitikrinkite asmens kodus. "+e.response.data+"!"):"Toks asmens kodas jau u\u017eimtas"===e.response.data?alert("Pasitikrinkite asmens kodus. "+e.response.data):"Gimimo data negali b\u016bti i\u0161 ateities!"===e.response.data?alert(e.response.data):"\u0160is asmens kodas jau egzistuoja sistemoje!"===e.response.data?alert("Pasitikrinkite ar suved\u0117te teisingus asmens kodus. "+e.response.data):"Invalid field entry"===e.response.data.message?alert("U\u017epildykite visus privalomus laukus!"):"The birthdate can't be from the future"===e.response.data?alert("Gimimo data negali b\u016bti i\u0161 ateities!"):"Bad birthdate format"===e.response.data.message?alert("Netinkamas datos formatas!"):"t\u0117vas/glob\u0117jas neregistruotas sistemoje"===e.response.data?alert("Registracija nes\u0117kminga. Pirmin\u0117 t\u0117vo registracijos forma turi b\u016bti u\u017epildyta pirma"):"t\u0117vas/glob\u0117jas neregistruotas sistemoje!"===e.response.data.message?alert("Registracija nes\u0117kminga. Pirmin\u0117 t\u0117vo/glob\u0117jo registracijos forma neu\u017epildyta"):400===e.response.status&&alert("Registracija nes\u0117kminga! Pasitikrinkite ar pa\u017eym\u0117jote bei u\u017epild\u0117te laukus teisingai!")}))},s.state={birthdate:new Date,parentId:"",id:"",firstname:"",lastname:"",personalCode:0,city:"",street:"",houseNumber:"",flatNumber:"",userId:"",secondParentId:"",secondParent:!1,secondParentFirstname:"",secondParentLastname:"",secondParentEmail:"",secondParentPhone:"",secondParentPersonalCode:0,secondParentCity:"",secondParentStreet:"",secondParentHouseNumber:"",secondParentFlatNumber:"",secondParentNumberOfKids:0,secondParentStudying:!1,secondParentStudyingInstitution:"",secondParentHasDisability:!1,secondParentDeclaredResidenceSameAsLiving:!1,secondParentDeclaredCity:"",secondParentDeclaredStreet:"",secondParentDeclaredHouseNumber:"",secondParentDeclaredFlatNumber:"",adopted:!1,errors:{firstname:"",lastname:"",personalCode:"",birthdate:"",city:"",street:"",houseNumber:"",flatNumber:"",secondParentFirstname:"",secondParentLastname:"",secondParentPersonalCode:"",secondParentEmail:"",secondParentPhone:"",secondParentCity:"",secondParentStreet:"",secondParentHouseNumber:"",secondParentFlatNumber:"",secondParentNumberOfKids:"",secondParentStudyingInstitution:"",secondParentDeclaredCity:"",secondParentDeclaredStreet:"",secondParentDeclaredHouseNumber:"",secondParentDeclaredFlatNumber:""}},s}return Object(s.a)(t,[{key:"componentDidMount",value:function(){var e=this;u.a.get("".concat(i.a,"/api/users/").concat(this.props.match.params.id,"/parentdetails")).then((function(a){e.setState({parentId:a.data.id,userId:e.props.match.params.id,city:a.data.city,street:a.data.street,houseNumber:a.data.houseNumber,flatNumber:a.data.flatNumber})})).catch((function(e){}))}},{key:"render",value:function(){var e=this.state.errors;return this.state.parentId>0?m.a.createElement("div",null,m.a.createElement("div",{className:"container mt-5 shadow p-3 mb-5 bg-white rounded"},m.a.createElement("div",{className:"mb-4"},m.a.createElement("h3",null,"Vaiko duomen\u0173 registracija")),m.a.createElement("form",{onSubmit:this.handleSubmit,className:"form-row "},m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"firstname",className:"control-label"},"Vaiko Vardas*:"),m.a.createElement("input",{type:"text",placeholder:"Vaiko Vardas",className:"form-control",name:"firstname",onChange:this.handleChange,noValidate:!0}),e.firstname.length>0&&m.a.createElement("span",{className:"error"},e.firstname)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"lastname",className:"control-label"},"Vaiko Pavard\u0117*:"),m.a.createElement("input",{type:"text",placeholder:"Vaiko Pavard\u0117",className:"form-control",name:"lastname",onChange:this.handleChange,noValidate:!0}),e.lastname.length>0&&m.a.createElement("span",{className:"error"},e.lastname)),m.a.createElement("div",{className:"form-group mb-3 col-6  "},m.a.createElement("label",{htmlFor:"birthdate",className:"control-label"},"Vaiko gimimo data*:"),m.a.createElement("div",null,m.a.createElement(b.a,{className:"form-control  ",dateFormat:"yyyy-MM-dd",locale:"lt",name:"birthdate",maxDate:new Date,selected:this.state.birthdate,onChange:this.handleChangeDate}))),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"personalCode",className:"control-label"},"Vaiko Asmens Kodas*:"),m.a.createElement("input",{type:"text",placeholder:"Asmens kodas",className:"form-control",name:"personalCode",onChange:this.handleChange,noValidate:!0}),e.personalCode.length>0&&m.a.createElement("span",{className:"error"},e.personalCode)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"city",className:"control-label"},"Miestas*:"),m.a.createElement("input",{type:"text",placeholder:"Miestas",className:"form-control",name:"city",value:this.state.city,onChange:this.handleChange,noValidate:!0}),e.city.length>0&&m.a.createElement("span",{className:"error"},e.city)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"street",className:"control-label"},"Gatv\u0117*:"),m.a.createElement("input",{type:"text",placeholder:"Gatv\u0117",className:"form-control",name:"street",value:this.state.street,onChange:this.handleChange,noValidate:!0}),e.street.length>0&&m.a.createElement("span",{className:"error"},e.street)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"houseNumber",className:"control-label"},"Namo Numeris*:"),m.a.createElement("input",{type:"text",placeholder:"Namo numeris",className:"form-control",name:"houseNumber",value:this.state.houseNumber,onChange:this.handleChange,noValidate:!0}),e.houseNumber.length>0&&m.a.createElement("span",{className:"error"},e.houseNumber)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"flatNumber",className:"control-label"},"Butas:"),m.a.createElement("input",{type:"number",min:"1",placeholder:"Butas",className:"form-control",name:"flatNumber",value:this.state.flatNumber,onChange:this.handleChange})),m.a.createElement("div",{className:"ml-4 form-check mb-3 col-12"},m.a.createElement("input",{className:"form-check-input",type:"checkbox",name:"adopted",checked:this.state.adopted,onChange:this.handleChange}),m.a.createElement("label",{htmlFor:"adopted",className:"form-check-label"},"Esu \u0161io vaiko Glob\u0117jas")),m.a.createElement("h5",{className:"mt-4 form-group mb-3 col-12"}," ","Antrojo t\u0117vo/glob\u0117jo duomenys"),m.a.createElement("div",{className:"ml-4 form-check mb-3 col-12"},m.a.createElement("input",{className:"form-check-input",type:"checkbox",name:"secondParent",checked:this.state.secondParent,onChange:this.handleChange}),m.a.createElement("label",{htmlFor:"secondParent",className:"form-check-label"},"Prid\u0117ti antr\u0105j\u012f \u0161io vaiko t\u0117v\u0105/glob\u0117j\u0105"),m.a.createElement("div",{className:"mt-3 ml-0"},m.a.createElement("b",null,"Prid\u0117j\u0119 antr\u0105j\u012f t\u0117v\u0105/glob\u0117j\u0105, v\u0117liau jo duomenis gal\u0117site redaguoti, bet pa\u0161alinti galima nebus."))),this.state.secondParent?m.a.createElement("div",{className:"form-row"},m.a.createElement("div",{className:"form-group mb-3 col-6 mt-3"},m.a.createElement("label",{htmlFor:"secondParentFirstname",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo vardas*:"),m.a.createElement("input",{type:"text",placeholder:"Vardas",className:"form-control",name:"secondParentFirstname",onChange:this.handleChange,noValidate:!0}),e.secondParentFirstname.length>0&&m.a.createElement("span",{className:"error"},e.secondParentFirstname)),m.a.createElement("div",{className:"form-group mb-3 col-6 mt-3"},m.a.createElement("label",{htmlFor:"secondParentLastname",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo pavard\u0117*:"),m.a.createElement("input",{type:"text",placeholder:"Pavard\u0117",className:"form-control",name:"secondParentLastname",onChange:this.handleChange,noValidate:!0}),e.secondParentLastname.length>0&&m.a.createElement("span",{className:"error"},e.secondParentLastname)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentEmail",className:"control-label"},"El.pa\u0161tas*:"),m.a.createElement("input",{type:"email",placeholder:"El.pa\u0161tas",className:"form-control",name:"secondParentEmail",onChange:this.handleChange,noValidate:!0}),e.secondParentEmail.length>0&&m.a.createElement("span",{className:"error"},e.secondParentEmail)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentPhone",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo tel.nr*:"),m.a.createElement("input",{type:"tel",placeholder:"Tel.nr",className:"form-control",name:"secondParentPhone",onChange:this.handleChange,noValidate:!0}),e.secondParentPhone.length>0&&m.a.createElement("span",{className:"error"},e.secondParentPhone)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentPersonalCode",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo asmens kodas*:"),m.a.createElement("input",{type:"text",placeholder:"Asmens kodas",className:"form-control",name:"secondParentPersonalCode",onChange:this.handleChange,noValidate:!0}),e.secondParentPersonalCode.length>0&&m.a.createElement("span",{className:"error"},e.secondParentPersonalCode)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentCity",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo miestas*:"),m.a.createElement("input",{type:"text",placeholder:"Miestas",className:"form-control",name:"secondParentCity",onChange:this.handleChange,noValidate:!0}),e.secondParentCity.length>0&&m.a.createElement("span",{className:"error"},e.secondParentCity)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentStreet",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo gatv\u0117*:"),m.a.createElement("input",{type:"text",placeholder:"Gatv\u0117",className:"form-control",name:"secondParentStreet",onChange:this.handleChange,noValidate:!0}),e.secondParentStreet.length>0&&m.a.createElement("span",{className:"error"},e.secondParentStreet)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentHouseNumber",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo namo numeris*:"),m.a.createElement("input",{type:"text",placeholder:"Namo numeris",className:"form-control",name:"secondParentHouseNumber",onChange:this.handleChange,noValidate:!0}),e.secondParentHouseNumber.length>0&&m.a.createElement("span",{className:"error"},e.secondParentHouseNumber)),m.a.createElement("div",{className:"form-group mb-3 col-4"},m.a.createElement("label",{htmlFor:"secondParentFlatNumber",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo butas:"),m.a.createElement("input",{type:"number",min:"0",placeholder:"Butas",className:"form-control",name:"secondParentFlatNumber",onChange:this.handleChange})),m.a.createElement("div",{className:"form-group mb-3 col-8"},m.a.createElement("label",{htmlFor:"secondParentNumberOfKids",className:"control-label"},"Kiek antrasis t\u0117vas/glob\u0117jas turi vaik\u0173, kurie mokosi pagal bendrojo ugdymo lavinimo programas?*"),m.a.createElement("input",{type:"number",min:"1",placeholder:"Skai\u010dius",className:"form-control",name:"secondParentNumberOfKids",onChange:this.handleChange,noValidate:!0,onInvalid:function(e){e.target.setCustomValidity("\u012eveskite vaik\u0173 skai\u010di\u0173.")},onInput:function(e){return e.target.setCustomValidity("")},required:!0}),e.secondParentNumberOfKids.length>0&&m.a.createElement("span",{className:"error"},e.secondParentNumberOfKids)),m.a.createElement("div",{className:"ml-4 form-check mb-3 col-12"},m.a.createElement("input",{className:"form-check-input",type:"checkbox",name:"secondParentStudying",checked:this.state.secondParentStudying,onChange:this.handleChange}),m.a.createElement("label",{htmlFor:"secondParentStudying",className:"form-check-label"},"Antrasis t\u0117vas/glob\u0117jas mokosi bendrojo lavinimo mokykloje")),this.state.secondParentStudying?m.a.createElement("div",{className:"mb-3"},m.a.createElement("label",{htmlFor:"secondParentStudyingInstitution",className:"control-label"},"Mokymosi \u012fstaigos pavadinimas*:"),m.a.createElement("input",{type:"text",placeholder:"Mokymosi \u012fstaiga",className:"form-control",name:"secondParentStudyingInstitution",onChange:this.handleChange,pattern:"[a-zA-Z-z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d . - 0-9-]+",onInvalid:function(e){e.target.setCustomValidity("\u012eveskite mokymosi \u012fstaigos pavadinim\u0105.")},onInput:function(e){return e.target.setCustomValidity("")},required:!0})):null,m.a.createElement("div",{className:"ml-4 form-check mb-3 col-12"},m.a.createElement("input",{type:"checkbox",className:"form-check-input",name:"secondParentHasDisability",id:"hasDisability",checked:this.state.secondParentHasDisability,onChange:this.handleChange,noValidate:!0}),m.a.createElement("label",{htmlFor:"secondParentHasDisability",className:"form-check-label"},"Antrasis t\u0117vas/glob\u0117jas ma\u017eesn\u012f nei 40% darbingumo lyg\u012f")),m.a.createElement("div",{className:"ml-4 form-check mb-3 col-12"},m.a.createElement("input",{className:"form-check-input",type:"checkbox",checked:this.state.secondParentDeclaredResidenceSameAsLiving,name:"secondParentDeclaredResidenceSameAsLiving",id:"secondParentDeclaredResidenceSameAsLiving",onChange:this.handleChange}),m.a.createElement("label",{htmlFor:"secondParentDeclaredResidenceSameAsLiving",className:"form-check-label"},"Jei deklaruota gyvenamoji vieta sutampa, pa\u017eym\u0117kite.")),this.state.secondParentDeclaredResidenceSameAsLiving?null:m.a.createElement("div",{className:"form-row"},m.a.createElement("div",{className:"form-group mb-3 col-6 mt-3"},m.a.createElement("label",{htmlFor:"secondParentDeclaredCity",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo deklaruotas miestas*:"),m.a.createElement("input",{type:"text",placeholder:"Deklaruotas Miestas",className:"form-control",name:"secondParentDeclaredCity",onChange:this.handleChange,pattern:"[a-zA-Z-z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d -]+",onInvalid:function(e){e.target.setCustomValidity("\u012eveskite deklaruot\u0105 miest\u0105 tinkamu formatu.")},onInput:function(e){return e.target.setCustomValidity("")},required:!0})),m.a.createElement("div",{className:"form-group mb-3 col-6 mt-3"},m.a.createElement("label",{htmlFor:"secondParentDeclaredStreet",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo deklaruota gatv\u0117*:"),m.a.createElement("input",{type:"text",placeholder:"Deklaruota Gatv\u0117",className:"form-control",name:"secondParentDeclaredStreet",onChange:this.handleChange,pattern:"^[a-zA-z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d ]+[- a-zA-z\u0105\u010d\u0119\u0117\u012f\u0161\u0173\u016b\u017e\u0104\u010c\u0118\u0116\u012e\u0160\u0172\u016a\u017d0-9 . -  ]*",onInvalid:function(e){e.target.setCustomValidity("\u012eveskite deklaruot\u0105 gatv\u0119 tinkamu formatu.")},onInput:function(e){return e.target.setCustomValidity("")},required:!0}),e.secondParentDeclaredStreet.length>0&&m.a.createElement("span",{className:"error"},e.secondParentDeclaredStreet)),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentDeclaredHouseNumber",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo deklaruotas namo numeris*:"),m.a.createElement("input",{type:"text",placeholder:"Deklaruotas Namo Numeris",className:"form-control",name:"secondParentDeclaredHouseNumber",onChange:this.handleChange,pattern:"^[1-9]+[ a-zA-Z 0-9 ]*",onInvalid:function(e){e.target.setCustomValidity("\u012eveskite deklaruot\u0105 namo numer\u012f tinkamu formatu, pvz.: 1A")},onInput:function(e){return e.target.setCustomValidity("")},required:!0})),m.a.createElement("div",{className:"form-group mb-3 col-6"},m.a.createElement("label",{htmlFor:"secondParentDeclaredFlatNumber",className:"control-label"},"Antrojo t\u0117vo/glob\u0117jo deklaruotas butas:"),m.a.createElement("input",{type:"number",min:"1",placeholder:"Deklaruotas Butas",className:"form-control",name:"secondParentDeclaredFlatNumber",onChange:this.handleChange})))):m.a.createElement("div",null),m.a.createElement("div",{className:"mt-3 form-group mb-3 col-6"}," ","* - privalomi laukai"),m.a.createElement("div",null,m.a.createElement("button",{type:"submit",className:"btn btn-block mt-5"},"I\u0161saugoti ir T\u0119sti"))))):m.a.createElement("div",null,m.a.createElement("p",null,"Pirma u\u017epildykite t\u0117vo/glob\u0117jo duomenis"))}}]),t}(c.Component)}}]);
//# sourceMappingURL=24.9b195a41.chunk.js.map