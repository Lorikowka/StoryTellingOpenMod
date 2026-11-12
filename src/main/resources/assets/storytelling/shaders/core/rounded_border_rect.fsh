#version 150

in vec2 fragUV;
out vec4 fragColor;

uniform float radius;
uniform vec2 size;

uniform vec4 fillColor;
uniform vec4 border2Color;
uniform vec4 border13Color;
uniform float borderSize;

float roundedBoxSDF(vec2 p, vec2 halfSize, float r) {
    if (r <= 0.01) {
        vec2 d = abs(p) - halfSize;
        return max(d.x, d.y);
    } else {
        vec2 d = abs(p) - halfSize + r;
        return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0) - r;
    }
}

float smoothedBand(float dist, float lower, float upper, float aa) {
    return smoothstep(lower - aa, lower + aa, dist) - smoothstep(upper - aa, upper + aa, dist);
}

void main() {
    vec2 halfSize = size * 0.5;
    vec2 p = fragUV * size - halfSize;
    float stepSize = borderSize / 3.0;

    vec2 innerSize = halfSize - vec2(borderSize);
    float dist = roundedBoxSDF(p, innerSize, radius);

    float aa = fwidth(dist);

    if (radius <= 0.0) aa = 0.5 * fwidth(dist);

    float innerAlpha = 1.0 - smoothstep(-aa, aa, dist - 0.0 * stepSize);
    float border3Alpha = smoothedBand(dist, stepSize * 0.0, stepSize * 1.0, aa);
    float border2Alpha = smoothedBand(dist, stepSize * 1.0, stepSize * 2.0, aa);
    float border1Alpha = smoothedBand(dist, stepSize * 2.0, stepSize * 3.0, aa);

    fragColor = (
    border13Color * border1Alpha +
    border2Color * border2Alpha +
    border13Color * border3Alpha +
    fillColor * innerAlpha
    );
}