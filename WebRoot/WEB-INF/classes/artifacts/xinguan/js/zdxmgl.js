  Ext.override(Ext.grid.GridPanel, {
        afterRender : Ext.Function.createSequence(Ext.grid.GridPanel.prototype.afterRender,
            function() {
                // 默认显示提示
                if (!this.cellTip) {
                    return;
                }

                var view = this.getView();

                this.tip = new Ext.ToolTip({
                    target: view.el,
                    delegate : '.x-grid-cell-inner',
                    trackMouse: true, 
                    renderTo: document.body, 
                    ancor : 'top',
                    style : 'background-color: #FFFFCC;',
                    listeners: {  
                        beforeshow: function updateTipBody(tip) {
                            //取cell的值
                            //fireFox  tip.triggerElement.textContent
                            //IE  tip.triggerElement.innerText 
                            var tipText = (tip.triggerElement.innerText || tip.triggerElement.textContent);
                            if (Ext.isEmpty(tipText) || Ext.isEmpty(tipText.trim()) ) {
                                return false;
                            }

                            tip.update(tipText);
                        }
                    }
                });
            })
    });