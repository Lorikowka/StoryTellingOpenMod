var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')

/**
 * Provide a key-value (name-class) pairs for classes to populate <auto> within.
 */
var autoClasses = {
    'FluidsST': 'com.storyteam.storytelling.init.FluidsST',
    'QuestsST': 'com.storyteam.storytelling.init.system.QuestsST'
};

function initializeCoreMod() {
    var coremods = {};
    for (var key in autoClasses) coremods[key] = createCoremod(key, autoClasses[key]);
    return coremods
}

function createCoremod(name, target) {
    return {
        'target': {
            'type': 'METHOD',
            'class': target,
            'methodName': "<clinit>",
            'methodDesc': "()V"
        },
        'transformer': function (node) {
            return transform(node, name);
        }
    }
}

function transform(node, name) {
    var prefix = name + " [auto]: ";
    ASMAPI.log('WARN', prefix + "Start")
    var count = 0
    var last = null
    var insn = node.instructions;
    var isize = insn.size();
    for (var x = 0; x < isize; x++) {
        var current = insn.get(x)
        if (current.getType() == 9 /*LDC_INSN*/ && current.cst == "<auto>") {
            var found = false
            for (var inner = x; inner < isize; inner++) {
                var sub = insn.get(inner)
                if (sub.name && sub.getOpcode() == Opcodes.PUTSTATIC) {
                    current.cst = sub.name.toLowerCase(); // lowercase
                    ASMAPI.log('WARN', prefix + "Populate {} with name {}", sub.name, current.cst);
                    ++count;
                    found = true;
                    break;
                }
            }
            if (!found) {
                ASMAPI.log('WARN', prefix + "Unable to find PUTSTATIC for <auto> instruction at {}", parseInt(x))
            }
        }
        last = current
    }
    ASMAPI.log('WARN', prefix + "Populated {} fields inside {}", count.toFixed(0), name)
    return node
}
