function jsAutoSubmit(formId, controlId) {
	window.setTimeout("TrPage._autoSubmit('" + formId + "', '" + controlId + "', new Object(), 1)", 200);
}

function changeAncestorClass(el, parentTag, skip, oldClass, newClass) {
	while (el && (el.tagName != parentTag || skip >= 0)) {
		el = el.parentNode;
		if (el.tagName == parentTag)
			skip--;
	}
	if (!el)
		return;
	var newClasses = el.className;
	if (!oldClass && newClass)
		newClasses += " "+newClass;
	else
		newClasses = newClasses.replace(oldClass, newClass);
	el.className = newClasses;
}

function addAncestorClass(el, parentTag, skip, newClass) {
	changeAncestorClass(el, parentTag, skip, null, newClass);
}

function removeAncestorClass(el, parentTag, skip, oldClass) {
	changeAncestorClass(el, parentTag, skip, oldClass, "");
}

function changeGranularityQuestionField(el) {
	var selClass = "granQuestSel";
	var radios = document.getElementsByName(el.name);
	for (var i = 0; i < radios.length; i++) {
		if (radios[i].checked) {
			addAncestorClass(radios[i], "TD", 1, selClass);
		} else {
			removeAncestorClass(radios[i], "TD", 1, selClass);
		}
	}
}
