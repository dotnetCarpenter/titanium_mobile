define(["Ti/_", "Ti/_/string", "Ti/Filesystem"], function(_, string, Filesystem) {

	var vp = require.config.vendorPrefixes.dom,
		is = require.is;

	function set(node, name, value) {
		var i = 0,
			x,
			uc;
		if (node) {
			if (arguments.length > 2) {
				while (i < vp.length) {
					x = vp[i++];
					x += x ? uc || (uc = string.capitalize(name)) : name;
					if (x in node.style) {
						require.each(is(value, "Array") ? value : [value], function(v) { node.style[x] = v; });
						return value;
					}
				}
			} else {
				for (x in name) {
					set(node, x, name[x]);
				}
			}
		}
		return node;
	}

	return {
		url: function(/*String|Blob*/url) {
			if (url && url.declaredClass === "Ti.Blob") {
				return "url(" + url.toString() + ")";
			}
			var match = url && url.match(/^(.+):\/\//),
				file = match && ~Filesystem.protocols.indexOf(match[1]) && Filesystem.getFile(url);
			return file && file.exists()
				? "url(" + file.read().toString() + ")"
				: !url || url === "none"
					? ""
					: /^url\(/.test(url)
						? url
						: "url(" + _.getAbsolutePath(url) + ")";
		},

		get: function(node, name) {
			if (is(name, "Array")) {
				for (var i = 0; i < name.length; i++) {
					name[i] = node.style[name[i]];
				}
				return name;
			}
			return node.style[name];
		},

		set: set
	};
});