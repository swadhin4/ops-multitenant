	
	// load visualization library
    //google.load('visualization', '1', {packages: ['geomap']});
	//google.charts.load('current', {'packages':['corechart']});
	google.charts.load('current', {'packages':['corechart', 'controls']});
	
	
	
	//google.charts.setOnLoadCallback(Hello('hello'));
	
	//function Hello(x){alert(x);};
    
	$(function(){
		google.charts.setOnLoadCallback(drawChart);
		google.charts.setOnLoadCallback(drawChartSP);
		google.charts.setOnLoadCallback(drawDashboard);
		google.charts.setOnLoadCallback(drawDashboardSP);
		
		$('#syncBtn').click(function(){
			google.charts.setOnLoadCallback(drawChart);
			google.charts.setOnLoadCallback(drawChartSP);
			google.charts.setOnLoadCallback(drawDashboard);
			google.charts.setOnLoadCallback(drawDashboardSP);
		});
	});	
	
     function drawChart() {
	
			//alert(x);
    		// when document loads, grab the json
			$.getJSON(webContextPath+"/dashboard/site/ticket/priority", function(data) {
				// once grabbed, we run this callback
				//alert(x);
				// setup the new map and its variables
				console.log(data);
				if(data.statusCode == 200){
				var DT = new google.visualization.DataTable();
				console.log(DT);
				console.log(data);
					DT.addRows(data.object.length);  // length gives us the number of results in our returned data
					DT.addColumn('string', 'Priority');
					DT.addColumn('number', 'Ticket Count');
					DT.addColumn({type:'string', label:'Site Name', role: 'annotation'});
	      		
				// now we need to build the map data, loop over each result
				$.each(data.object, function(i,v) {
					// set the values for both the name and the population
					DT.setValue(i, 0, v.priorityId);
					DT.setValue(i, 1, v.ticketCount);
					DT.setValue(i, 2, v.siteName);
				});
				
				// Create a dashboard.
				var dashboard = new google.visualization.Dashboard(
				document.getElementById('dashboard_div'));
				
				
				// Create a range slider, passing some options
				var donuCategoryFilter = new google.visualization.ControlWrapper({
				  'controlType': 'CategoryFilter',
				  'containerId': 'filter_site_pie_div',
				  'options': {
					'filterColumnLabel': 'Site Name',
					'ui': {'allowNone': false, 'allowMultiple': false, 'allowTyping': false}
				  }
				});

				// Create a pie chart, passing some options
				var pieChart = new google.visualization.ChartWrapper({
				  'chartType': 'PieChart',
				  'containerId': 'chart_site_pie_div',
				  'options': {
					'width': 600,
					'height': 250,
					'animation':{
								startup: true,
								duration: 500,
								easing: 'out',
							  },
					'pieSliceText': 'value',
					'legend': 'right',
					'title': 'Tickets By Priority',
					'is3D':true
				  }
				});
				
				//dashboard.bind(donutRangeSlider, pieChart);
				
				dashboard.bind(donuCategoryFilter, pieChart);
				
				dashboard.draw(DT);	
				}else{
					
				}
			});
   }
   
   function drawChartSP() {
	
			//alert(x);
    		// when document loads, grab the json
			$.getJSON(webContextPath+"/dashboard/sp/ticket/priority", function(data) {
				// once grabbed, we run this callback
				//alert(x);
				// setup the new map and its variables
				if(data.statusCode == 200){
				var DT = new google.visualization.DataTable();
				console.log(DT);
					DT.addRows(data.object.length);  // length gives us the number of results in our returned data
					DT.addColumn('string', 'Priority');
					DT.addColumn('number', 'Ticket Count');
					DT.addColumn({type:'string', label:'Service Provider', role: 'annotation'});
	      		
				// now we need to build the map data, loop over each result
				$.each(data.object, function(i,v) {
					// set the values for both the name and the population
					DT.setValue(i, 0, v.priorityId);
					DT.setValue(i, 1, v.ticketCount);
					DT.setValue(i, 2, v.serviceProviderName);
				});
				
				// Create a dashboard.
				var dashboard = new google.visualization.Dashboard(
				document.getElementById('dashboard_SP_div'));
				
				
				// Create a range slider, passing some options
				var donuCategoryFilter = new google.visualization.ControlWrapper({
				  'controlType': 'CategoryFilter',
				  'containerId': 'filter_SP_pie_div',
				  'options': {
					'filterColumnLabel': 'Service Provider',
					'ui': {'allowNone': false, 'allowMultiple': false, 'allowTyping': false}
				  }
				});

				// Create a pie chart, passing some options
				var pieChart = new google.visualization.ChartWrapper({
				  'chartType': 'PieChart',
				  'containerId': 'chart_SP_pie_div',
				  'options': {
					'width': 600,
					'height': 250,
					'animation':{
								startup: true,
								duration: 500,
								easing: 'out',
							  },
					'pieSliceText': 'value',
					'legend': 'right',
					'title': 'Tickets By Priority',
					'is3D':true
				  }
				});
				
				//dashboard.bind(donutRangeSlider, pieChart);
				
				dashboard.bind(donuCategoryFilter, pieChart);
				
				dashboard.draw(DT);	
				}else{
				}
			});
   }
   
   function drawDashboard() {

        // when document loads, grab the json
			$.getJSON(webContextPath+"/dashboard/site/ticket/status", function(data) {
				// once grabbed, we run this callback
				if(data.statusCode == 200){
				// setup the new map and its variables
				var DT = new google.visualization.DataTable();
					DT.addRows(data.object.length);  // length gives us the number of results in our returned data
					DT.addColumn('string', 'status');
					DT.addColumn('number', 'Ticket Count');
					DT.addColumn({type:'string', label:'Site Name', role: 'annotation'});
					//DT.addColumn({type: 'string', role: 'style'});
	      		
				// now we need to build the map data, loop over each result
				$.each(data.object, function(i,v) {
					// set the values for both the name and the population
					DT.setValue(i, 0, v.status);
					DT.setValue(i, 1, v.ticketCount);
					DT.setValue(i, 2, v.siteName);
					//DT.setValue(i, 2, v.site_name);
				});
				
				//var view = new google.visualization.DataView(DT);
				//view.setColumns([0,1,2]);
				
				var view = new google.visualization.DataView(DT);
				view.setColumns([0,1,2, {calc:barColor, type:'string', label:'Bar color', role: 'style'}]);
				
				function barColor(DT, rowNum){
				/*   if (DT.getValue(rowNum, 0) == "Raised") {
					return '#b8860b';
				  } else if (DT.getValue(rowNum, 0) == "Logged") {
					return "#b8860b";
				  }
				  else {
					return '#b8860b';
					} */
				  return '#b8860b';
				  }
				
				// Create a dashboard.
				var dashboard = new google.visualization.Dashboard(
				document.getElementById('dashboard_div'));
				
				// Create a range slider, passing some options
				var donuCategoryFilter = new google.visualization.ControlWrapper({
				  'controlType': 'CategoryFilter',
				  'containerId': 'filter_site_bar_div',
				  'options': {
					'filterColumnLabel': 'Site Name',
					'ui': {'allowNone': false, 'allowMultiple': false, 'allowTyping': false}
				  }
				});
				
			
				 // Create a pie chart, passing some options
				var barChart = new google.visualization.ChartWrapper({
				  'chartType': 'BarChart',
				  'containerId': 'chart_site_bar_div',
				  'options': {
					'width': "100%",
					'height': 250,
					'legend': {position: 'none'},
					'pieSliceText': 'value',
					'title': 'Tickets By Status',
					'bars': 'horizontal',
					'animation':{
								startup: true,
								duration: 500,
								easing: 'out',
							  },

					 'hAxis': {
				              'format': 0
				          },
				          'chartArea': {left: 120, width: '100%', height:'70%'}
				     }
				     
				});
				
				//dashboard.bind(donutRangeSlider, pieChart);
				
				dashboard.bind(donuCategoryFilter, barChart);
				
				dashboard.draw(view);	
				
				}else{
				}

			});
      }
	  
	  function drawDashboardSP() {

        // when document loads, grab the json
			$.getJSON(webContextPath+"/dashboard/sp/ticket/status", function(data) {
				// once grabbed, we run this callback
				if(data.statusCode == 200){
				// setup the new map and its variables
				var DT = new google.visualization.DataTable();
					DT.addRows(data.object.length);  // length gives us the number of results in our returned data
					DT.addColumn('string', 'status');
					DT.addColumn('number', 'Ticket Count');
					DT.addColumn({type:'string', label:'Service Provider', role: 'annotation'});
					//DT.addColumn({type: 'string', role: 'style'});
	      		
				// now we need to build the map data, loop over each result
				$.each(data.object, function(i,v) {
					// set the values for both the name and the population
					DT.setValue(i, 0, v.status);
					DT.setValue(i, 1, v.ticketCount);
					DT.setValue(i, 2, v.serviceProviderName);
					//DT.setValue(i, 2, v.site_name);
				});
				
				//var view = new google.visualization.DataView(DT);
				//view.setColumns([0,1,2]);
				
				var view = new google.visualization.DataView(DT);
				view.setColumns([0,1,2, {calc:barColor, type:'string', label:'Bar color', role: 'style'}]);
				
				function barColor(DT, rowNum){
				/*   if (DT.getValue(rowNum, 0) == "Raised") {
					return '#1E90FF';
				  } else if (DT.getValue(rowNum, 0) == "Logged") {
					return "#1E90FF";
				  }
				  else {
					return '#1E90FF';
					} */
				  return '#1E90FF';
				  }
				
				// Create a dashboard.
				var dashboard = new google.visualization.Dashboard(
				document.getElementById('dashboard_SP_div'));
				
				// Create a range slider, passing some options
				var donuCategoryFilter = new google.visualization.ControlWrapper({
				  'controlType': 'CategoryFilter',
				  'containerId': 'filter_SP_bar_div',
				  'options': {
					'filterColumnLabel': 'Service Provider',
					'ui': {'allowNone': false, 'allowMultiple': false, 'allowTyping': false}
				  }
				});
				
			
				 // Create a pie chart, passing some options
				var barChart = new google.visualization.ChartWrapper({
				  'chartType': 'BarChart',
				  'containerId': 'chart_SP_bar_div',
				  'options': {
					'width': "100%",
					'height': 250,
					'legend': {position: 'none'},
					'pieSliceText': 'value',
					'title': 'Tickets By Status',
					'bars': 'horizontal',
					'animation':{
								startup: true,
								duration: 500,
								easing: 'out',
							  },
					'hAxis': {
			              'format': 0
			          },
			          'chartArea': {left: 120, width: '100%', height:'70%'}
				  }
				});
				
				//dashboard.bind(donutRangeSlider, pieChart);
				
				dashboard.bind(donuCategoryFilter, barChart);
				
				dashboard.draw(view);	
				}else{
				}
			});
      }