var hasJQuery = (typeof($) !="undefined");
if(hasJQuery){
	$(function(){
		onPageLoad();
	})
}else{
	var s = document.createElement("script");
	s.src="js/jquery.js";
	s.type="text/javascript";
	s.onload = function(){
		$(function(){onPageLoad();});
	}
	document.head.appendChild(s);
}

function onPageLoad(){
}

// 图片点击
function initImage(){
	var filter = ["ExpandedBlockStart.gif","ContractedBlock.gif"];
    $("img").click(function(e){
                	var src = $(this).attr("src");
                	if($(this).attr("id")=="blog_avatar") return; // 过滤头像
                	if(src.indexOf("ExpandedBlockStart.gif")!=-1|| src.indexOf("ContractedBlock.gif")!=-1){
                	    return false;
                	}

                   e.stopPropagation(); // 阻止事件冒泡
                	var urls = new Array();
                	$("img").each(function(key,obj){
                        var itemUrl = $(obj).attr("src");
                	    // 过滤图片
                	    if($(obj).attr("id")=="blog_avatar") return; // 过滤头像
                	    if(itemUrl.indexOf("ExpandedBlockStart.gif")!=-1|| itemUrl.indexOf("ContractedBlock.gif")!=-1|| itemUrl.indexOf("copycode.gif")!=-1){
                            return;
                        }
                		urls[urls.length] = itemUrl;
                	});
                	var images ="{}"
                    if(urls.length>0){
                        images = JSON.stringify(urls);
                    }
                	app.onImageClick(src, images);
                	return false;
                });
}


// 重新设置代码高亮
function refreshCodeTheme(){

    $(".cnblogs_code span").each(function(i,e){
        var color = $(e).css("color");
        if(color){
            color = rgb2hex(color);
            // 关键字
            if(color=="#0000ff"){
                $(e).css("color","").addClass("code_key_primary");
            }
            // 注释
            else if(color=="#008000"){
                $(e).css("color","").addClass("code_comment");
            }
            // 参数
            else if(color == "#000000"){
                $(e).css("color","").addClass("code_args");
            }
            // 分号
            else if(color == "#800080"){
                $(e).css("color","").addClass("code_symbol");
            }
            // 字符串
            else if(color == "#800000"){
                $(e).css("color","").addClass("code_text");
            }
            // 突出显示
            else if(color == "#ff0000"){
                $(e).css("color","").addClass("code_high_night");
            }
        }

    })

}

function rgb2hex(rgb) {
rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
function hex(x) {
return ("0" + parseInt(x).toString(16)).slice(-2);
}
return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
}

function getImageWidth(url,callback){
    var img = new Image();
    img.src = url;
    
    // 如果图片被缓存，则直接返回缓存数据
    if(img.complete){
        callback(img.width, img.height);
    }else{
         // 完全加载完毕的事件
        img.onload = function(){
             callback(img.width, img.height);
        }
    }

    
}