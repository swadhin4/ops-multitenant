function UiModification() {

	jQuery("#mainHeader").hide();
	jQuery("#subHeader").hide();
	
	jQuery("#backbutton").click(function(){
	location.reload();
	});
}