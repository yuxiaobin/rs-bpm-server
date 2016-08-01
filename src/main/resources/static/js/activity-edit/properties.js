/**
 * Created by Administrator on 16-5-22.
 */
var PROPERTY_TYPE_BOOLEAN = "Boolean";
var PROPERTY_TYPE_ENUM = "Enum";
var PROPERTY_TYPE_DATE = "Date";
var newProperty = function(){
    $("#propertyTable").append("<tr>"
        +"<td><input type=\"text\" name=\"newPropertyName\" class=\"newPropertyName\"placeholder='Property Name'></td>"
        +"<td><select name=\"newPropertyType\" onchange='changePropertyType(this)'>"
        +"<option value='String'>String</option>"
        +"<option value='Integer'>Integer</option>"
        +"<option value='Boolean'>Boolean</option>"
        +"<option value='Date'>Date</option>"
        +"<option value='Enum'>Enum</option>"
        +"<option value='User'>User</option>"
        +"<option value='Group'>Group</option>"
        +"</select>"
        +"</td>"
        +"<td class=\"dy string\"><input type=\"text\" name=\"newPropertyValue\">"
        +"<div class=\"close_span\" onclick=\"removeProperty(this)\">x</div>"
        +"</td>"
        +"<td class=\"dy boolean\" style=\"display:none\">" +
        "<input type=\"radio\" name=\"newPropertyValue\" value=\"true\">" +
        "<input type=\"radio\" name=\"newPropertyValue\" value=\"false\">" +
        "</td>"
        +"<td class=\"dy enum\" style=\"display:none\"><input type=\"text\" name=\"newPropertyValue\" placeholder='enum todo'></td>"
        +"<td class=\"dy date\" style=\"display:none\"><input type=\"text\" name=\"newPropertyValue\">"

        +"</td>"
        +"</tr>");
}
var changePropertyType = function(obj){
    var td_parent = $(obj).parent();
    var selected_type = $(obj).val();
    if(PROPERTY_TYPE_BOOLEAN==selected_type){
        console.log("boolean selected");
        td_parent.siblings(".dy").each(function(){
            if($(this).hasClass("boolean")){
                $(this).css("display","");
            }else{
                $(this).css("display","none");
            }
        });
    }else if(PROPERTY_TYPE_ENUM==selected_type){
        console.log("enum selected");
        td_parent.siblings(".dy").each(function(){
            if($(this).hasClass("enum")){
                $(this).css("display","");
            }else{
                $(this).css("display","none");
            }
        });
    }else if(PROPERTY_TYPE_DATE == selected_type){
        console.log("date selected");
        td_parent.siblings(".dy").each(function(){
            if($(this).hasClass("date")){
                $(this).css("display","");
            }else{
                $(this).css("display","none");
            }
        });
    }
}

var removeProperty = function(obj){
    var selectedTr = $(obj).parents("tr");
//    console.log(selectedTr);
    selectedTr.remove();
}

/*
 propertiesArray:[{name,type,value},...]
 */
var loadProperties = function(propertiesArray){
    if(propertiesArray!=undefined){
        for(i=0;i<propertiesArray.length;++i){
            var property_ = propertiesArray[i];
            $("#propertyTable").append("<tr>"
                +"<td><input type=\"text\" name=\"newPropertyName\" class=\"newPropertyName\"placeholder='Property Name' value='"+property_.name+"'></td>"
                +"<td><select name=\"newPropertyType\" onchange='changePropertyType(this)'>"
                +"<option value='String'>String</option>"
                +"<option value='Integer'>Integer</option>"
                +"<option value='Boolean'>Boolean</option>"
                +"<option value='Date'>Date</option>"
                +"<option value='Enum'>Enum</option>"
                +"<option value='User'>User</option>"
                +"<option value='Group'>Group</option>"
                +"</select>"
                +"</td>"
                +"<td class=\"dy string\"><input type=\"text\" name=\"newPropertyValue\" value='"+property_.value+"'>"
                +"<div class=\"close_span\" onclick=\"removeProperty(this)\">x</div>"
                +"</td>"
                +"<td class=\"dy boolean\" style=\"display:none\">" +
                "<input type=\"radio\" name=\"newPropertyValue\" value=\"true\">" +
                "<input type=\"radio\" name=\"newPropertyValue\" value=\"false\">" +
                "</td>"
                +"<td class=\"dy enum\" style=\"display:none\"><input type=\"text\" name=\"newPropertyValue\" placeholder='enum todo'></td>"
                +"<td class=\"dy date\" style=\"display:none\"><input type=\"text\" name=\"newPropertyValue\">"

                +"</td>"
                +"</tr>");
        }
    }

}