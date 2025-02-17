	function bignum() {
		var height1 = images1.height;
		var width1 = images1.width;
		images1.height = height1 * 5;
		images1.width = width1 * 5;
		autobig() ;
	}
	
	function bignum2() {
		var height1 = images1.height;
		var width1 = images1.width;
		images1.height = height1 * 3;
		images1.width = width1 * 3;
		autobig() ;
	}
	
	function bignum3() {
		var height1 = images1.height;
		var width1 = images1.width;
		images1.height = height1 * 1.8;
		images1.width = width1 * 1.8;
		autobig() ;
	}
	
	function smallit() {
		var height1 = images1.height;
		var width1 = images1.width;
		images1.height = height1 / 1.2;
		images1.width = width1 / 1.2;
		autobig() ;
	}

	function bigit() {
		var height1 = images1.height;
		var width1 = images1.width;
		images1.height = height1 * 1.2;
		images1.width = width1 * 1.2;
		autobig() ;
	}
	
	function bigitAuto() {
		var height1 = images2.height;
		var width1 = images2.width;
		images1.height = height1 * 1.8;
		images1.width = width1 * 1.8;
	}
	function smallAuto() {
		var height1 = images2.height;
		var width1 = images2.width;
		images1.height = height1 * 1.2;
		images1.width = width1 * 1.2;
	}
	function big1p5() {
		var height1 = images2.height;
		var width1 = images2.width;
		images1.height = height1 * 1.5;
		images1.width = width1 * 1.5;
	}
	
	function realsize() {
		images1.height = images2.height;
		images1.width = images2.width;
		images1.style.left = 0;
		images1.style.top = 0;

		polygon.style.top = 0;
		polygon.style.left = 0;
		polygon.style.height = 0;
		polygon.style.width = 0;
		
		//优替补录，录天津胸科送检单时，备注为四个框，重置时需要重置掉另外三个
		var a = document.getElementById('polygon1');
		if(a){
			polygon1.style.top = -8;
			polygon1.style.left = -33;
			polygon1.style.height = 0;
			polygon1.style.width = 0;
			polygon2.style.top = -8;
			polygon2.style.left = -33;
			polygon2.style.height = 0;
			polygon2.style.width = 0;
			polygon3.style.top = -8;
			polygon3.style.left = -33;
			polygon3.style.height = 0;
			polygon3.style.width = 0;
		}
	}
	function autobig() {
		polygon.style.top = -8;
		polygon.style.left = -33;
		polygon.style.height = 0;
		polygon.style.width = 0;
	}
 
	//小定位
    function  smalladdress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			smallAuto();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
    //直接定位
    function  directAddress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
    		realsize();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
	//普通定位
    function  faddress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			bigitAuto();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
    
    
    //放大定位
    function  bigfaddress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			bignum2();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
  //超大定位
    function  hugefaddress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			bignum();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
    
    //中定位
    function  zhongfaddress(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			bignum3();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
    //1.5倍放大
    function  bignn(ileft,itop,pwidth,pheight,pleft,ptop){
    	if (autoTrack == 1) {
			realsize();
			big1p5();
			images1.style.top = itop;
			images1.style.left = ileft;
			polygon.style.top =  ptop;
			polygon.style.left = pleft;
			polygon.style.height = pheight;
			polygon.style.width = pwidth;
			return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
		}
		
    }
    
  //优替定位
    function  ytDirectAddress(ileft,itop,pwidth,pheight,pleft,ptop,pwidth1,pheight1,pleft1,ptop1,pwidth2,pheight2,pleft2,ptop2,pwidth3,pheight3,pleft3,ptop3){
    	if (autoTrack == 1) {
    		realsize();
    		images1.style.top = itop;
    		images1.style.left = ileft;
    		polygon.style.top =  ptop;
    		polygon.style.left = pleft;
    		polygon.style.height = pheight;
    		polygon.style.width = pwidth;
    		polygon1.style.top =  ptop1;
    		polygon1.style.left = pleft1;
    		polygon1.style.height = pheight1;
    		polygon1.style.width = pwidth1;
    		polygon2.style.top =  ptop2;
    		polygon2.style.left = pleft2;
    		polygon2.style.height = pheight2;
    		polygon2.style.width = pwidth2;
    		polygon3.style.top =  ptop3;
    		polygon3.style.left = pleft3;
    		polygon3.style.height = pheight3;
    		polygon3.style.width = pwidth3;
    		return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
    	}
    	
    }
    function ytdw(x){
    	if(x=='bz'){
    		if(oldOrNew==1){
    			return ytDirectAddress(0,0,230,27,330,80,230,27,330,147,560,25,0,170,160,30,240,290); 
    		}else{
    			realsize();
    		}
    	}
    	if(x=='address'){
    		if(oldOrNew==1){
    			return smalladdress(-130,0,460,30,80,165); 
    		}else{
    			return bigfaddress(-155,-238,605,60,-40,220); 
    		}
    	}
    	if(x=='age'){
    		if(oldOrNew==1){
    			return smalladdress(-300,0,280,30,95,120); 
    		}else{
    			return bigfaddress(-510,-115,240,120,230,190);
    		}
    	}
    	if(x=='bloodtime'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-160,-1200,440,120,-40,140);
    		}
    	}
    	if(x=='blzd'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-200,0,330,40,150,260);
    		}
    	}
    	if(x=='cslqph'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-470,-1000,290,120,140,190);
    		}
    	}
    	if(x=='cslqppd'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-170,-1000,490,120,-40,190);
    		}
    	}
    	if(x=='cslqpz'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-170,-1000,490,120,-40,190);
    		}
    	}
    	if(x=='cxxzz'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-970,-1000,350,120,30,190);
    		}
    	}
    	if(x=='cxxzzpd'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-970,-1000,350,120,30,190);
    		}
    	}
    	if(x=='cztc'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-333,610,40,-40,260);
    		}
    	}
    	if(x=='danwei'){
    		if(oldOrNew==1){
    			return smalladdress(0,-150,290,30,10,118);
    		}else{
    			return bigfaddress(-170,-500,425,120,-40,90); 
    		}
    	}
    	if(x=='date'){
    		if(oldOrNew==1){
    			return smalladdress(-300,-800,200,35,150,120);
    		}else{
    			return bigfaddress(-1250,-2300,410,90,30,140);
    		}
    	}
    	if(x=='doctor'){
    		if(oldOrNew==1){
    			return smalladdress(0,-150,290,30,10,140);
    		}else{
    			return bigfaddress(-850,-500,450,120,80,90); 
    		}
    	}
    	if(x=='doctordate'){
    		if(oldOrNew==1){
    			return smalladdress(-300,-800,200,35,150,148);
    		}else{
    			return bigfaddress(-550,-2300,420,90,20,240);
    		}
    	}
    	if(x=='doctorsign'){
    		if(oldOrNew==1){
    			return smalladdress(0,-800,450,35,5,148);
    		}else{
    			return bigfaddress(-150,-2300,440,90,-40,240);
    		}
    	}
    	if(x=='doctortelphone'){
    		if(oldOrNew==1){
    			return smalladdress(-250,-150,380,30,45,140);
    		}else{
    			return bigfaddress(-1250,-500,450,80,110,120); 
    		}
    	}
    	if(x=='fadx'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-333,610,40,-40,260);
    		}
    	}
    	if(x=='familyhistory'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,210,30,10,228);
    		}else{
    			return bigfaddress(-155,-300,605,60,-40,210); 
    		}
    	}
    	if(x=='familyrelation'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,190,30,210,228);
    		}else{
    			return bigfaddress(-768,-300,530,60,-40,210); 
    		}
    	}
    	if(x=='familysickage'){
    		if(oldOrNew==1){
    			return smalladdress(-200,0,280,30,195,228);
    		}else{
    			return bigfaddress(-1278,-300,520,60,-25,210); 
    		}
    	}
    	if(x=='guardianrelation'){
    		if(oldOrNew==1){
    			return smalladdress(0,-800,150,35,310,120);
    		}else{
    			return bigfaddress(-850,-2300,350,90,70,140);
    		}
    	}
    	if(x=='guardiansign'){
    		if(oldOrNew==1){
    			return smalladdress(0,-800,155,35,160,120);
    		}else{
    			return bigfaddress(-550,-2300,350,90,0,140);
    		}
    	}
    	if(x=='jiguan'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,190,30,210,140); 
    		}else{
    			return bigfaddress(-1220,-115,500,120,30,190);
    		}
    	}
    	if(x=='jzcadx'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-333,610,40,-40,260);
    		}
    	}
    	if(x=='keshi'){
    		if(oldOrNew==1){
    			return smalladdress(-250,-150,380,30,45,118);
    		}else{
    			return bigfaddress(-570,-500,380,120,-10,90); 
    		}
    	}
    	if(x=='lbjzyc'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-185,410,40,100,260);
    		}
    	}
    	if(x=='lcfq'){
    		if(oldOrNew==1){
    			return smalladdress(0,-150,290,30,10,204);
    		}else{
    			return smalladdress(-65,-123,610,30,-40,230);
    		}
    	}
    	if(x=='lczd'){
    		if(oldOrNew==1){
    			return directAddress(0,-50,560,28,0,205);
    		}else{
    			return bigfaddress(-570,-550,405,60,-40,150);
    		}
    	}
    	if(x=='lyqg'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-170,-1000,400,120,-40,90);
    		}
    	}
    	if(x=='mail'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-1285,-195,530,60,-40,200); 
    		}
    	}
    	if(x=='name'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,210,30,10,120); 
    		}else{
    			return bigfaddress(-20,-115,340,100,80,210);
    		}
    	}
    	if(x=='nation'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,210,30,10,140); 
    		}else{
    			return bigfaddress(-760,-115,260,120,230,190);
    		}
    	}
    	if(x=='num' || x=='num2' || x=='num3'  || x=='num1' ){
    		if(oldOrNew==1){
    			return smalladdress(0,0,400,35,0,95); 
    		}else{
    			return bigfaddress(-1340,50,400,280,130,50); 
    		}
    	}
    	if(x=='qita'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-160,-1200,340,120,-40,95);
    		}
    	}
    	if(x=='qtjc'){
    		if(oldOrNew==1){
    			return directAddress(0,-250,550,28,0,230);
    		}else{
    			return smalladdress(-65,-333,610,40,-40,270);
    		}
    	}
    	if(x=='qtsm'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,550,28,0,293);
    		}else{
    			return smalladdress(-60,-333,610,30,-40,230);
    		}
    	}
    	if(x=='rxadx'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-333,610,40,-40,260);
    		}
    	}
    	if(x=='sex'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,190,30,210,120); 
    		}else{
    			return bigfaddress(-250,-115,260,100,210,190);
    		}
    	}
    	if(x=='sfzh'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,210,30,10,160); 
    		}else{
    			return bigfaddress(-165,-195,600,60,-40,210);    		
    		}
    	}
    	if(x=='sickage'){
    		if(oldOrNew==1){
    			return smalladdress(-250,-150,190,30,235,204);
    		}else{
    			return bigfaddress(-160,-550,425,60,-40,150);
    		}
    	}
    	if(x=='sickhistory'){
    		if(oldOrNew==1){
    			return smalladdress(0,0,390,30,10,185);
    		}else{
    			return smalladdress(-200,0,430,40,70,160); 
    		}
    	}
    	if(x=='sign'){
    		if(oldOrNew==1){
    			return smalladdress(0,-800,160,35,5,118);
    		}else{
    			return bigfaddress(-150,-2300,420,90,-40,130);
    		}
    	}
    	if(x=='sslqph'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-470,-1000,290,120,140,190);
    		}
    	}
    	if(x=='sslqppd'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-170,-1000,490,120,-40,190);
    		}
    	}
    	
    	if(x=='sslqpz'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-170,-1000,490,120,-40,190);
    		}
    	}
    	if(x=='sxxzz'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-970,-1000,350,120,30,190);
    		}
    	}
    	if(x=='sxxzzpd'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-970,-1000,350,120,30,190);
    		}
    	}
    	if(x=='telphone'){
    		if(oldOrNew==1){
    			return smalladdress(-300,0,280,30,95,140); 
    		}else{
    			return bigfaddress(-755,-195,520,60,-40,210); 
    		}
    	}
    	if(x=='tid'){}
    	if(x=='wadx'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-333,610,40,-40,260);
    		}
    	}
    	if(x=='xfshui'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return bigfaddress(-160,-1200,340,120,-40,50);
    		}
    	}
    	if(x=='xyyw'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,560,28,0,178);
    		}else{
    			return smalladdress(-65,-123,610,30,-40,250);
    		}
    	}
    	if(x=='ybfz'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-185,410,40,100,260);
    		}
    	}
    	if(x=='yczyc'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-185,410,40,100,260);
    		}
    	}
    	if(x=='zlfy'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,560,28,0,198);
    		}else{
    			return smalladdress(-65,-123,610,30,-40,270);
    		}
    	}
    	if(x=='zzms'){
    		if(oldOrNew==1){
    			return directAddress(0,-50,560,28,0,223);
    		}else{
    			return directAddress(0,0,560,40,0,235);
    		}
    	}
    	if(x=='checkprojectpd1'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='checkprojectpd2'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='checkprojectpd3'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='checkprojectpd4'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='checkprojectpd5'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='checkprojectpd6'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,40,-40,220);	
    		}
    	}
    	if(x=='lyqg1'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,175,28,0,235);
    		}else{
    			return smalladdress(-65,-373,610,160,-40,60);	
    		}
    	}
    	if(x=='ybfz1'||x=='lbjzyc1'||x=='yczyc1'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,385,28,170,235);
    		}else{
    			return smalladdress(-65,-373,610,160,-40,60);	
    		}
    	}
    	if(x=='yblx1'||x=='piece1'||x=='spiece1'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,550,28,0,255);
    		}else{
    			return smalladdress(-65,-373,610,160,-40,60);	
    		}
    	}
    	if(x=='bloodtime1'){
    		if(oldOrNew==1){
    			return directAddress(0,-150,550,28,0,273);
    		}else{
    			return smalladdress(-65,-373,610,160,-40,60);	
    		}
    	}
    	if(x=='yblx2'||x=='yblx3'||x=='piece2'||x=='piece3'||x=='spiece2'||x=='spiece3'||x=='bloodtime2'||x=='bloodtime3'||x=='lyqg2'||x=='lyqg3'||x=='ybfz2'||x=='ybfz3'||x=='lbjzyc2'||x=='lbjzyc3'||x=='yczyc2'||x=='yczyc3'){
    		if(oldOrNew==1){
    			realsize();
    		}else{
    			return smalladdress(-65,-373,610,160,-40,60);	
    		}
    	}
    	
    }
  
  
  //优逸定位
    function yydw(x){
    	if(x=='num'){
    		if (autoTrack == 1) {
				realsize();
				bignum();
				images1.style.top = '0px';
				images1.style.left = '-2500px';
				polygon.style.top = '10px';
				polygon.style.left = '20px';
				polygon.style.height = '200px';
				polygon.style.width = '450px';
			}
    		return 'west_panel.body.dom.scrollTop = 0;west_panel.body.dom.scrollLeft=0;';
    	}
    	if(x=='hospital'){
    	    return bigfaddress('-40px','-250px','500px','70px','60px','100px');	
    	}
    	if(x=='yblx'){
    	    return bigfaddress('-700px','-250px','500px','50px','60px','100px');	
    	}
    	if(x=='yblxpd'){
    	    return bigfaddress('-950px','-250px','200px','70px','200px','80px');	
    	}
    	if(x=='ybdate'){
    		return bigfaddress('-1300px','-250px','400px','70px','50px','80px');	
    	}
    	if(x=='name'){
    		return bigfaddress('-40px','-250px','500px','80px','60px','140px');		
    	}
    	if(x=='sex'){
    		return bigfaddress('-400px','-250px','250px','80px','150px','140px');		
    	}
    	if(x=='swm'){
    		return zhongfaddress('-60px','-200px','550px','60px','-20px','320px');		
    	}
    	if(x=='harmful'){
    		return zhongfaddress('-200px','-200px','550px','60px','0px','350px');		
    	}
    	if(x=='jcxm'){
    		return directAddress('0px','0px','550px','60px','0px','340px');		
    	}
    	if(x=='age'){
    		return bigfaddress('-600px','-250px','300px','80px','150px','140px');		
    	}
    	if(x=='idcard'){
    		return faddress('40px','0px','450px','50px','60px','260px');		
    	}
    	if(x=='nation'){
    		return bigfaddress('-900px','-250px','250px','80px','150px','140px');		
    	}
    	if(x=='cellphone'){
    		return faddress('-340px','0px','320px','50px','100px','260px');		
    	}
    	if(x=='origin'){
    		return bigfaddress('-1100px','-250px','250px','80px','180px','140px');		
    	}
    	if(x=='email'){
    		return faddress('-640px','0px','350px','50px','100px','260px');		
    	}
    	if(x=='address'){
    		return directAddress('0px','0px','550px','30px','0px','150px');	
    	}
    	if(x=='grjbs'){
    		return bigfaddress('-40px','-500px','500px','80px','60px','120px');	
    	}
    	if(x=='lcbx'){
    		return bigfaddress('-40px','-500px','500px','80px','60px','180px');
    	}
    	if(x=='familyhistory'){
    		return faddress('0px','-200px','450px','50px','40px','260px');
    	}
    	if(x=='fbnl'){
    		return bigfaddress('-400px','-500px','400px','80px','120px','120px');	
    	}
    	if(x=='zlqk'){
			return directAddress('0px','0px','430px','30px','140px','210px');
    	}
    	if(x=='familyrelation'){
    		return faddress('-400px','-200px','450px','50px','40px','260px');
    	}
    	if(x=='lczd'){
    		return zhongfaddress('-550px','-200px','550px','60px','-20px','160px');
    	}
    	if(x=='qtjbs'){
    		return directAddress('0px','0px','550px','30px','0px','230px');
    	}
    	if(x=='familysickage'){
    		return faddress('-600px','-200px','200px','50px','250px','260px');
    	}
    	if(x=='familylczd'){
    		return directAddress('0px','0px','550px','30px','0px','265px');
    	}
    	if(x=='swmpd'){
    		return zhongfaddress('-60px','-200px','550px','60px','-20px','320px');
    	}
    	if(x=='harmfulpd'){
    		return zhongfaddress('-200px','-200px','550px','60px','0px','350px');
    	}
    	if(x=='jcxmpd'){
    		return directAddress('0px','0px','550px','60px','0px','340px');
    	}
    	if(x=='sjzqm'){
    		return bigfaddress('-40px','-1900px','450px','120px','40px','200px');	
    	}
    	if(x=='doctorqm'){
    		return bigfaddress('-40px','-1900px','470px','110px','70px','300px');		
    	}
    	if(x=='jhrqm'){
    		return bigfaddress('-540px','-1900px','450px','120px','0px','200px');		
    	}
    	if(x=='doctordate'){
    		return bigfaddress('-500px','-1900px','450px','120px','40px','280px');		
    	}
    	if(x=='yhzgx'){
    		return bigfaddress('-850px','-1900px','400px','120px','80px','200px');		
    	}
    	if(x=='sjdate'){
    		return bigfaddress('-1250px','-1900px','400px','120px','80px','200px');		
    	}
    	if(x=='relatives1'){
    		return bigfaddress('-40px','-2100px','350px','70px','70px','310px');		
    	}
    	if(x=='relativessex1'){
    		return bigfaddress('-300px','-2100px','230px','70px','130px','300px');		
    	}
    	if(x=='relativesage1'){
    		return bigfaddress('-500px','-2100px','230px','70px','130px','300px');		
    	}
    	if(x=='guanxi1'){
    		return bigfaddress('-730px','-2100px','350px','70px','110px','300px');		
    	}
    	if(x=='biaoxian1'){
    		return bigfaddress('-1200px','-2100px','610px','70px','-40px','300px');	
    	}
    	if(x=='relatives2'){
    		return bigfaddress('-40px','-2100px','350px','70px','70px','350px');
    	}
    	if(x=='relativessex2'){
    		return bigfaddress('-300px','-2100px','230px','70px','130px','350px');
    	}
    	if(x=='relativesage2'){
    		return bigfaddress('-500px','-2100px','230px','70px','130px','350px');
    	}
    	if(x=='guanxi2'){
    		return bigfaddress('-730px','-2100px','350px','70px','110px','350px');
    	}
    	if(x=='biaoxian2'){
    		return bigfaddress('-1200px','-2100px','610px','70px','-40px','350px');
    	}
    	if(x=='qsqm'){
    		return bigfaddress('-40px','-2200px','350px','80px','70px','360px');
    	}
    	if(x=='qsdate'){
    		return bigfaddress('-1160px','-2200px','450px','80px','100px','350px');
    	}
    	if(x=='bz'){
    		return directAddress('0px','0px','0px','0px','-33px','8px');
    	}
    }
    
    function msidw(x){
    	if(x=='name'){
    		return bigfaddress('-100px','-250px','500px','120px','60px','80px');	 
    	}
    	if(x=='sex'){
    		return bigfaddress('-600px','-250px','500px','120px','60px','80px');	 
    	}
    	if(x=='age'){
    		return bigfaddress('-1150px','-250px','600px','120px','-30px','80px');	 
    	}
    	if(x=='nation'){
    		return bigfaddress('-100px','-250px','500px','70px','60px','180px');	 
    	}
    	if(x=='origin'){
    		return bigfaddress('-600px','-250px','500px','70px','60px','180px');	 
    	}
    	if(x=='phone'){
    		return bigfaddress('-1150px','-250px','600px','70px','-30px','180px');	 
    	}
    	if(x=='address'){
    		return directAddress('0px','0px','550px','25px','0px','150px');		
    	}
    	if(x=='jzbs'){
    		return directAddress('0px','0px','550px','25px','0px','165px');		
    	}
    	if(x=='jwbs'){
    		return directAddress('0px','0px','550px','25px','0px','185px');		
    	}
    	if(x=='hospital'){
    		return directAddress('0px','0px','400px','40px','0px','210px');		
    	}
    	if(x=='doctor'){
    		return directAddress('0px','0px','200px','40px','350px','210px');		
    	}
    	if(x=='blzd'){
    		return directAddress('0px','0px','550px','25px','0px','235px');		
    	}
    	if(x=='lcfq'){
    		return directAddress('0px','0px','550px','25px','0px','255px');		
    	}
    	if(x=='mlh1'){
    		return bigfaddress('-200px','-650px','300px','60px','140px','200px');	 
    	}
    	if(x=='msh2'){
    		return bigfaddress('-500px','-650px','250px','60px','140px','200px');	 
    	}
    	if(x=='pms2'){
    		return bigfaddress('-800px','-650px','270px','60px','90px','200px');	 
    	}
    	if(x=='msh6'){
    		return bigfaddress('-1000px','-650px','300px','60px','140px','200px');	 
    	}
    	if(x=='nd'){
    		return bigfaddress('-1200px','-650px','200px','120px','200px','180px');	 
    	}
    	if(x=='num'){
    		return bigfaddress('-1330px','20px','600px','300px','-30px','40px'); 
    	}
    	if(x=='ssyb'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='sslqp'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='sxxzz'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='sslqpz'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='sslqph'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='sxxzzpd'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='ccyb'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='cslqp'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='cslqpz'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='cslqph'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='cxxzz'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='cxxzzpd'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='wzx'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='wzxpd'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='ybdate'){
    		return directAddress('0px','0px','550px','70px','0px','300px');		
    	}
    	if(x=='bz'){
    		return directAddress('0px','-100px','550px','40px','0px','250px');		
    	}
    	if(x=='sjzqm'){
    		return bigfaddress('-100px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='jhrqm'){
    		return bigfaddress('-450px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='guanxi'){
    		return bigfaddress('-800px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='sjdate'){
    		return bigfaddress('-1170px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='doctorqm'){
    		return bigfaddress('-180px','-2300px','550px','120px','-30px','200px');	 
    	}
    	if(x=='doctordate'){
    		return bigfaddress('-1170px','-2300px','420px','120px','30px','200px');	 
    	}
	 }
    
    function jy21dw(x){
    	if(x=='name'){
    		return bigfaddress('-100px','-250px','530px','80px','20px','140px');	 
    	}
    	if(x=='sex'){
    		return bigfaddress('-600px','-250px','470px','80px','20px','140px');	 
    	}
    	if(x=='age'){
    		return bigfaddress('-1130px','-250px','530px','80px','-10px','140px');	 
    	}
    	if(x=='nation'){
    		return bigfaddress('-100px','-320px','530px','80px','20px','140px');	 
    	}
    	if(x=='origin'){
    		return bigfaddress('-600px','-320px','530px','80px','20px','140px');	 
    	}
    	if(x=='phone'){
    		return bigfaddress('-1130px','-320px','530px','80px','-10px','140px');	 
    	}
    	if(x=='address'){
    		return directAddress('0px','0px','550px','40px','0px','155px');		
    	}
    	if(x=='hospital'){
    		return directAddress('0px','0px','340px','40px','0px','195px');		
    	}
    	if(x=='doctor'){
    		return directAddress('0px','0px','200px','40px','350px','195px');		
    	}
    	if(x=='rxalx'){
    		return directAddress('0px','0px','500px','60px','0px','220px');		
    	}
    	if(x=='lxs'){
    		return directAddress('0px','0px','500px','60px','0px','220px');		
    	}
    	if(x=='t'){
    		return bigfaddress('-300px','-700px','500px','80px','0px','160px');		
    	}
    	if(x=='n'){
    		return bigfaddress('-300px','-700px','500px','80px','0px','160px');		
    	}
    	if(x=='m'){
    		return bigfaddress('-300px','-700px','500px','80px','0px','160px');		
    	}
    	if(x=='zq'){
    		return bigfaddress('-300px','-800px','500px','80px','0px','130px');		
    	}
    	if(x=='zqpd'){
    		return directAddress('0px','-100px','350px','40px','190px','185px');		
    	}
    	if(x=='ts'){
    		return bigfaddress('-300px','-860px','500px','80px','0px','130px');		
    	}
    	if(x=='tspd'){
    		return directAddress('0px','-120px','350px','40px','190px','185px');		
    	}
    	if(x=='fts'){
    		return bigfaddress('-300px','-920px','500px','80px','0px','130px');		
    	}
    	if(x=='ftspd'){
    		return directAddress('0px','-140px','350px','40px','190px','185px');		
    	}
    	if(x=='fjbz'){
    		return directAddress('0px','-170px','250px','40px','190px','185px');		
    	}
    	if(x=='fj'){
    		return directAddress('0px','-190px','300px','40px','100px','185px');		
    	}
    	if(x=='er'){
    		return directAddress('0px','-215px','300px','40px','100px','185px');		
    	}
    	if(x=='pr'){
    		return directAddress('0px','-215px','300px','40px','100px','185px');		
    	}
    	if(x=='her2'){
    		return directAddress('0px','-215px','300px','40px','100px','185px');		
    	}
    	if(x=='ki67'){
    		return directAddress('0px','-215px','300px','40px','100px','185px');		
    	}
    	if(x=='num'){
    		return bigfaddress('-1330px','20px','600px','300px','-30px','40px'); 
    	}
    	if(x=='zslqp'){
    		return directAddress('0px','-255px','270px','40px','100px','185px');		
    	}
    	if(x=='zslqpz'){
    		return directAddress('0px','-255px','270px','40px','100px','185px');		
    	}
    	if(x=='zslqph'){
    		return directAddress('0px','-255px','270px','40px','100px','185px');		
    	}
    	if(x=='xxzz'){
    		return directAddress('0px','-255px','200px','40px','350px','185px');		
    	}
    	if(x=='xxzzpd'){
    		return directAddress('0px','-255px','200px','40px','350px','185px');		
    	}
    	if(x=='ybdate'){
    		return bigfaddress('-300px','-1300px','450px','80px','70px','140px');		
    	}
    	if(x=='bz'){
    		return directAddress('0px','-300px','550px','40px','0px','185px');		
    	}
    	if(x=='sjzqm'){
    		return bigfaddress('-100px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='jhrqm'){
    		return bigfaddress('-450px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='guanxi'){
    		return bigfaddress('-800px','-2300px','420px','120px','30px','120px');	 
    	}
    	if(x=='sjdate'){
    		return bigfaddress('-1170px','-2300px','420px','80px','30px','120px');	 
    	}
    	if(x=='doctorqm'){
    		return bigfaddress('-180px','-2300px','550px','120px','-30px','200px');	 
    	}
    	if(x=='doctordate'){
    		return bigfaddress('-1170px','-2300px','420px','80px','30px','200px');	 
    	}
	}
    
    function knub_dw(x){
		if(x=='chuzhen'||x=='fufa'||x=='isshoushu2'||x=='isshoushu1'){
            return zhongfaddress('-20px','-160px','380px','40px','30px','260px');
		}
        if(x=='sssj'||x=='qtzl1'||x=='qtzl2'){
            return zhongfaddress('-420px','-160px','380px','40px','30px','260px');
        }
        if(x=='qtzl'){
            return zhongfaddress('-620px','-160px','380px','40px','30px','260px');
        }
		if(x=='isxys2' || x=='isxys1'){
			return zhongfaddress('-533px','-50px','300px','50px','10px','210px');
		}
		if(x=='isjj1' || x=='isjj2'){
			return zhongfaddress('-750px','-50px','300px','50px','10px','210px');
		}
    	if(x=='jcxm'){
    		return directAddress('0px', '0px', '555px', '100px', '0px', '275px');
    	}
		if(x=='name'){
			return bigfaddress("-40px","-50px","460px","80px","40px","220px");
		}
		if(x=='sex'){
			return bigfaddress("-500px","-50px","200px","80px","40px","220px");
		}
		if(x=='age'){
			return bigfaddress("-700px","-50px","200px","80px","40px","220px");
		}
		if(x=='sfzh'){
			return zhongfaddress('-500px','30px','480px','50px','50px','185px');
		}
		if(x=='phone'){
			return bigfaddress("-40px","-50px","460px","60px","40px","280px");
		}
		if(x=='email'){
			return bigfaddress("-500px","-50px","400px","60px","40px","280px");
		}
		if(x=='address'){
			return directAddress('0px','0px','500px','35px','50px','110px');
		}
		if(x=='grjbs'){
			return zhongfaddress('-450px','50px','350px','50px','130px','230px');
		}
		if(x=='zljzs'){
            return zhongfaddress('-100px','-20px','555px','50px','0px','240px');
		}
		if(x=='hospital'){
			return zhongfaddress('-20px','-65px','500px','40px','30px','260px');
		}
		if(x=='sjks'){
			return zhongfaddress('-520px','-65px','230px','40px','30px','260px');
		}
		if(x=='doctor'){
			return zhongfaddress('-520px','-65px','250px','40px','255px','260px');
		}
		if(x=='qznl'){
			return zhongfaddress('-20px','-130px','220px','40px','30px','260px');
		}
		if(x=='lczd'){
			return zhongfaddress('-240px','-130px','390px','40px','30px','260px');
		}
        if(x=='blfx'){
            return zhongfaddress('-240px','-130px','390px','40px','30px','260px');
        }
		if(x=='lcfq'){
			return zhongfaddress('-620px','-130px','380px','40px','30px','260px');
		}
        if(x=='lbjzy'){
            return zhongfaddress('-620px','-130px','380px','40px','30px','260px');
        }
		if(x=='jcjg'){
    		return bigfaddress('-200px','-540px','555px','50px','0px','240px');
    	}
		if(x=='zls'){
    		return directAddress('0px','0px','555px','25px','0px','260px');
    	}

        if(x=='er'){
            return zhongfaddress('-590px','-190px','108px','40px','50px','260px');
        }

        if(x=='pr'){
            return zhongfaddress('-590px','-190px','105px','40px','155px','260px');
        }

        if(x=='her2'){
            return zhongfaddress('-590px','-190px','250px','40px','255px','260px');
        }

        if(x=='ki67'){
            return zhongfaddress('-590px','-190px','250px','40px','360px','260px');
        }
		if (x == 'lyqg'|| x == 'ybfz'|| x == 'lbjzyc'|| x == 'yczyc'|| x == 'sslqp'
			 || x == 'sslqpz'|| x == 'sslqph'|| x == 'sxxzz'|| x == 'sxxzzg'|| x == 'cslqp'
			 || x == 'cslqpz'|| x == 'cslqph'|| x == 'cxxzz'|| x == 'cxxzzg'|| x == 'xfs'
			 || x == 'xfsml'|| x == 'wzx'|| x == 'wzxml'|| x == 'kqsz'|| x == 'qt'
			 || x == 'qtpd'|| x == 'ybdate'|| x == 'qtsm') {
			return directAddress('0px', '0px', '555px', '100px', '0px', '275px');
		}
		if(x=='zongyt'||x=='ytbz'||x=='ytgj'||x=='ytdx'||x=='ytdxpd'||x=='ythl'||x=='ytmsi'||x=='yt21'||x=='ytqt'||x=='ytqtpd'
			||x=='zongyxctd'||x=='yxctbz'||x=='yxctgj'||x=='yxctdx'||x=='yxctdxpd'||x=='yxqt'||x=='yxqtpd'
			||x=='zongyxctc'||x=='yxctcjzca'||x=='yxctcqt'||x=='yxctcqtpd'
			||x=='zongyy'||x=='yyqu'||x=='yymale'||x=='yyfemale'||x=='yybrca'||x=='yydx'||x=='yydxpd'||x=='yyqt'||x=='yyqtpd'){
			return directAddress('0px', '-200px', '555px', '180px', '0px', '180px');
		}
		if(x=='yt'||x=='yxct'||x=='yxctc'||x=='yy'){
			return bigfaddress('-20px', '-1210px', '390px', '504px', '65px', '-14px');
		}
		if(x=='sjzqm'){
			return bigfaddress("-20px","-2250px","440px","70px","70px","210px");
		}
		if(x=='jhrqm'){
			return bigfaddress("-460px","-2250px","350px","70px","70px","210px");
		}
		if(x=='relation'){
			return bigfaddress("-810px","-2250px","380px","70px","70px","210px");
		}
		if(x=='sjzdate'){
			return bigfaddress("-1220px","-2250px","430px","70px","70px","210px");
		}
		if(x=='doctorqm'){
			return bigfaddress("-20px","-2250px","440px","70px","70px","270px");
		}
		if(x=='doctordate'){
			return bigfaddress("-1220px","-2250px","430px","70px","70px","260px");
		}
		if(x=='bz'){
			return directAddress('0px','0px','0px','0px','-33px','-8px');
		}
	}
    //优馨益客服补录
    function yxy_dw(x){
		if(x=='sjdw'){
			return bigfaddress("-40px","-115px","460px","80px","40px","220px");
		}
		if(x=='sjks'){
			return bigfaddress("-500px","-115px","200px","80px","40px","220px");
		}
		if(x=='collectiontime'){
			return bigfaddress("-700px","-115px","200px","80px","40px","220px");
		}
		if(x=='name'){
			return zhongfaddress('-500px','0px','480px','40px','50px','195px');
		}
		if(x=='xname'){
			return bigfaddress("-40px","-115px","460px","60px","40px","280px");
		}
		if(x=='telphone'){
			return bigfaddress("-500px","-115px","400px","60px","40px","280px");
		}
		if(x=='age'){
			return zhongfaddress('-500px','0px','480px','40px','50px','230px');
		}
		if(x=='birth'){
			return zhongfaddress('-20px','0px','500px','40px','30px','260px');
		}
		if(x=='gender'){
			return zhongfaddress('-500px','-20px','360px','70px','150px','225px');
		}
		if(x=='hospital'){
			return zhongfaddress('-20px','-65px','500px','40px','30px','260px');
		}
		if(x=='address'){
			return bignn(-85,0,600,60,-35,230);
		}
		if(x=='liucyc'||x=='crrstyc'||x=='trrstyc'||x=='bbrstyc' ){
			return zhongfaddress('-200px','-65px','560px','100px','-10px','250px');
		}
		if(x=='liuc'||x=='cngtdefect'||x=='blycs'){
			return bignn(-140,-100,560,130,0,215);
		}
		if(x=='sampleType'||x=='sampleqt1'){
			return bignn(-140,-100,560,90,0,320);	
    	}
		if(x=='haveweekw'){
			return bigfaddress("-850px","-600px","450px","80px","100px","300px");
		}
		if(x=='lczd'){
			return bignn(-70,-200,600,50,-35,290);
		}
		if(x=='isclose'){
			return bignn(-70,-200,600,50,-35,315);
		}
		if(x=='jqjh'){
			return bignn(-260,-200,600,50,-35,315);
		}
		if(x=='fbloodtype'){
			return bignn(-70,-200,600,45,-35,340);
		}
		if(x=='ismore'){
			return bignn(-260,-200,420,45,145,340);
		}
		if(x=='liucc'||x=='liucs'){
			return bignn(-70,-200,600,45,-35,360);
		}
		if(x=='liuctype'||x=='lchaveweekw'){
			return bignn(-260,-200,420,45,145,360);
		}
		if(x=='gngr'){
			return bignn(-70,-200,600,40,-35,385);
		}
		if(x=='torch'){
			return bignn(-260,-200,420,40,145,385);
		}
		if(x=='myliuc'){
			return bignn(-180,-200,600,40,-35,405);
		}
		if(x=='myjcms'){
			return bignn(-260,-200,420,40,145,405);
		}
		if(x=='rst'||x=='rstyc'){
			return bignn(-180,-200,600,45,-35,420);
		}
		if(x=='jcmethod'){
			return bignn(-260,-200,420,45,145,420);
		}
		if(x=='mbloodtype'){
			return bignn(-180,-200,600,45,-35,445);
		}
		if(x=='szjc'){
			return bignn(-260,-200,420,45,145,445);
		}
		if(x=='mrst'||x=='mrstyc'){
			return bignn(-180,-200,600,45,-35,465);
		}
		if(x=='mjcmethod'){
			return bignn(-260,-200,420,45,145,465);
		}
		if(x=='bc'){
			return bignn(-70,-205,600,45,-35,485);
		}
		if(x=='bcyc'){
			return bignn(-260,-205,500,45,45,485);
		}
		
		if(x=='xq'){
			return bignn(-140,-300,600,45,-35,415);
		}
		if(x=='santi21'||x=='santi18'||x=='ntd'||x=='ytz'){
			return bignn(-260,-320,500,45,45,415);
		}
		if(x=='lcrst'||x=='lcrstyc'){
			return bignn(-180,-325,600,45,-35,430);
		}
		if(x=='lcjcmethod'){
			return bignn(-260,-325,200,45,365,425);
		}
		if(x=='other'){
			return bignn(-180,-345,600,45,-35,430);
		}
		if(x=='bfamilyhistory'||x=='familyhistory2'){
			return bignn(-180,-370,600,50,-35,430);
		}
		if(x=='jwbs'||x=='jwhistory2'){
			return bignn(-180,-400,600,45,-35,430);
		}
		if(x=='yysys'||x=='kss'||x=='byy'||x=='yyother'||x=='yysystime'||x=='ksstime'||x=='byytime'||x=='yyothertime'){
			return bignn(-260,-420,600,70,-35,430);
		}
		if(x=='jclxbr'||x=='jclxcp'||x=='familyjc'){
			return bignn(-160,-550,600,120,-35,350);
		}
		if(x=='couplename'||x=='names1'||x=='gx1'||x=='names2'||x=='gx2'||x=='names3'||x=='gx3'){
			return bignn(-260,-550,600,120,-35,370);
		}
		if(x=='sjzqz'){
			return bignn(-60,-840,600,60,-35,350);
		}
		if(x=='sfzh'){
			return bignn(-290,-830,500,60,80,350);
		}
		if(x=='dlrqz'){
			return bignn(-60,-870,600,60,-35,350);
		}
		if(x=='dlrqzdate'){
			return bignn(-290,-860,420,60,145,370);
		}
		if(x=='bz'){
			return bignn(-90,-660,600,60,-35,350);
		}
	}
    //地贫客服补录
    function th_dw(x){
    	if(x=='ybbh'){
    		return bigfaddress("-1200px","0px","460px","200px","40px","50px");
    	}
		if(x=='sjdw'){
			return bignn("-50px","-50px","460px","60px","-30px","95px");
		}
		if(x=='sampleType'){
			return bignn("-350px","-50px","460px","60px","40px","95px");
		}
		if(x=='sjzname'){
			return bignn("-50px","-50px","460px","60px","-30px","120px");
		}
		if(x=='collectionTime'){
			return bignn("-350px","-50px","460px","60px","40px","120px");
		}
		if(x=='xsjzname'){
			return bignn("-50px","-50px","460px","60px","-30px","150px");
		}
		if(x=='age'){
			return bignn("-350px","-50px","460px","60px","40px","150px");
		}
		if(x=='gender'){
			return bignn("-50px","-50px","460px","60px","-30px","180px");
		}
		if(x=='nation'){
			return bignn("-350px","-50px","460px","60px","40px","180px");
		}
		if(x=='jiguan'){
			return bignn("-50px","-50px","460px","60px","-30px","210px");
		}
		if(x=='mobile'){
			return bignn("-350px","-50px","460px","60px","40px","210px");
		}
		if(x=='address'){
			return zhongfaddress("-50px","-50px","550px","80px","-30px","290px");
		}
		if(x=='wysx'){
			return bignn("-50px","-260px","270px","60px","-30px","55px");
		}
		if(x=='qgyz'){
			return bignn("-220px","-260px","270px","60px","20px","55px");
		}if(x=='gxbzl'){
			return bignn("-350px","-260px","270px","60px","20px","55px");
		}
		if(x=='myzl'){
			return bignn("-380px","-260px","270px","60px","220px","55px");
		}
		if(x=='cpth'){
			return zhongfaddress("-150px","-125px","600px","80px","-30px","290px");
		}
		if(x=='fatherth'){
			return zhongfaddress("-150px","-160px","600px","80px","-30px","290px");
		}
		if(x=='motherth'){
			return zhongfaddress("-150px","-200px","600px","80px","-30px","290px");
		}
		if(x=='selfth'){
			return zhongfaddress("-150px","-235px","600px","80px","-30px","290px");
		}
		if(x=='excpsm'){
			return zhongfaddress("-50px","-270px","550px","80px","-30px","290px");
		}
		if(x=='sjzqz'){
			return bignn("-30px","-770px","350px","60px","-30px","380px");
		}
		if(x=='cardType'){
			return bignn("-315px","-770px","550px","60px","-15px","380px");
		}
		if(x=='ysqz'){
			return bignn("-30px","-800px","350px","60px","-30px","380px");
		}
		if(x=='cardid'){
			return bignn("-315px","-800px","550px","60px","-15px","380px");
		}
		if(x=='qzDate'){
			return bignn("-315px","-800px","460px","60px","75px","420px");
		}
	}
    
    //处理键盘事件 禁止后退键（Backspace）密码或单行、多行文本框除外
	function banBackSpace(e) {
		var ev = e || window.event; //获取event对象
		var obj = ev.target || ev.srcElement; //获取事件源

		var t = obj.type || obj.getAttribute('type'); //获取事件源类型

		//获取作为判断条件的事件类型
		var vReadOnly = obj.getAttribute('readonly');
		var vEnabled = obj.getAttribute('enabled');
		//处理null值情况
		vReadOnly = (vReadOnly == null) ? false : vReadOnly;
		vEnabled = (vEnabled == null) ? true : vEnabled;

		//当敲Backspace键时，事件源类型为密码或单行、多行文本的，
		//并且readonly属性为true或enabled属性为false的，则退格键失效
		var flag1 = (ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea")
			 && (vReadOnly == true || vEnabled != true)) ? true : false;

		//当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效
		var flag2 = (ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea")
		 ? true : false;

		//判断
		if (flag2) {
			return false;
		}
		if (flag1) {
			return false;
		}
	}

	//禁止后退键 作用于Firefox、Opera
	document.onkeypress = banBackSpace;
	//禁止后退键 作用于IE、Chrome
	document.onkeydown = banBackSpace;