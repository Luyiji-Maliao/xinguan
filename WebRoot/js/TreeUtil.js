Ext.namespace("TreeUtil");

TreeUtil={

	//函数: 级联选择, 作为tree的checkchange事件处理函数
	cascadeCheck:function(node,checked){
		//调用函数改变子孙节点的状态
		TreeUtil.changeChildrenStatus(node,checked);
		//调用函数改变祖先节点的状态
		TreeUtil.changeParentStatus(node);

	},


	//如果节点前的checkbook已被选中, 返回true, 否则返回false
	isChecked:function(node){
		//alert("node.data.checked:"+node.data.checked);
		return node.data.checked;
	},


	//将节点前的checkbook的选中状态设为checked
	setChecked:function(node, checked){
		
			//node.data.checked=checked;
		
			node.set({checked:checked});
	},


	//得到一个节点下所有被选中的节点, 返回值为一节点数组 
	getSelected:function(root){
		var selected=[];
		
		if(TreeUtil.isChecked(root)){
			selected.push(root);
		}
	//	alert(root.data.text);
		root.eachChild(function(child){
			
			selected=selected.concat(TreeUtil.getSelected(child));
		});

		 return selected;
	},


	//函数:得到节点的兄弟节点,包括自己
	getSiblings:function(node){
		var siblings=[];
		var parent=node.parentNode;
		if(parent){
			parent.eachChild(function(sibling){
				siblings.push(sibling);
			});
		}
		return siblings;
	},

	//函数: 改变子孙节点的状态
	changeChildrenStatus:function(node,checked){
		//alert("node.hasChildNodes()："+node.hasChildNodes());
		//alert(node.data.text);
		if(node.hasChildNodes()){
			node.eachChild(function(child){
				//alert("child:::"+child);
				TreeUtil.changeChildrenStatus(child,checked);
			});
		}
		node.set({checked:checked});
		//node.data.checked=checked;
	},

		//函数: 改变祖先节点的状态
	changeParentStatus:function(node){
		var parent=node.parentNode;
		if(!parent){
			return ;
		}

		var siblings=TreeUtil.getSiblings(node);

		var checked=false;
		for(var i=0;i<siblings.length;i++){
			if(TreeUtil.isChecked(siblings[i])){
				checked=true;
				break;
			}
		}
		
		TreeUtil.setChecked(parent, checked);
		
		TreeUtil.changeParentStatus(parent);
	}
}