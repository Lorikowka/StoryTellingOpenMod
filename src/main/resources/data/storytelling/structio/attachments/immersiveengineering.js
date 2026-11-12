const ModHelper = Java.type("org.zeith.hammerlib.util.mcf.ModHelper");
const GlobalWireNetwork = Java.type("blusunrize.immersiveengineering.api.wires.GlobalWireNetwork");
const Connection = Java.type("blusunrize.immersiveengineering.api.wires.Connection");

const knownWireTypes = new java.util.HashSet();
const isKnownWireType = (type) => {
    if(knownWireTypes.isEmpty())
        knownWireTypes.addAll(Java.type("blusunrize.immersiveengineering.common.blocks.metal.EnergyConnectorBlockEntity").NAME_TO_SPEC.keySet().stream().map(s => s.toString()).toList());
    return knownWireTypes.contains(type);
};

//<editor-fold desc="Core">
function test() {
    return ModHelper.isModLoaded("immersiveengineering");
}

function gather(level, structure, attachment) {
    let subs = [];
    processPositiveConnections(level, structure, (pos, target, connection) => subs.push(attachment.sub(connection.toNBT())));
    attachment.setSubs("wires", subs);
}

function prePlace(level, structure, attachment) {
    const net = GlobalWireNetwork.getNetwork(level);
    const connections = [];
    processPositiveConnections(level, structure, (pos, target, connection) => connections.push(connection));
    connections.forEach(e => net.removeConnection(e))
}

function place(level, structure, attachment) {
    const wires = attachment.getSubs("wires");
    if (!wires || wires.isEmpty()) return;
    const net = GlobalWireNetwork.getNetwork(level);
    wires.forEach(s => net.addConnection(new Connection(s.data)))
}

//</editor-fold>

function processPositiveConnections(level, structure, handler) {
    const net = GlobalWireNetwork.getNetwork(level);

    forEachWireConnector(level, structure, (pos) => {
        const lnet = net.getLocalNet(pos);
        if (!lnet) return;
        lnet.getConnections(pos).forEach(connection => {
            const target = connection.getEndFor(pos);
            if (connection.isPositiveEnd(target)) {
                handler(pos, target, connection);
            }
        });
    });
}

function forEachWireConnector(level, structure, handler) {
    structure.sections().forEach(data => {
        const allTiles = data.tiles;
        allTiles.positions().forEach(pos => {
            const type = allTiles.getTypeAt(pos);
            if (type && isKnownWireType(type)) {
                handler(pos);
            }
        })
    });
}