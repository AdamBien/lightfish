

if(typeof(lightfish)=='undefined'){
    lightfish = {};
}

if(typeof(lightfish.escalation)=='undefined'){
    lightfish.escalation = {};
}

lightfish.escalation.email = {
    _formName:"notificationConfiguration\\:",
    _defaults:{
        gmail:{
            protocol: 'SMTPS',
            host:'smtp.gmail.com',
            port: 465,
            authorization: true
        }
    },
    loadDefaults:function(provider){
        var defaults = lightfish.escalation.email._defaults[provider];
        var form = "#" + lightfish.escalation.email._formName;
        for(var key in defaults){
            var elem = $(form + key);
            if(elem.is(':checkbox')){
                elem.attr('checked', Boolean(defaults[key]))
            }else{
                elem.val(defaults[key]);
            }
        }
    },
    setup:function(){
        for(var key in lightfish.escalation.email._defaults){
            $("#provider_" + key).click(function(e){
                e.preventDefault();
                lightfish.escalation.email.loadDefaults(key);
            });
        }
    }
}

$(function(){
    lightfish.escalation.email.setup();
})