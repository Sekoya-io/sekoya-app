{
    const FIT_PADDING_POINTS = 170;
    const FIT_PADDING_FALLBACK = 20;
    const FIT_MAX_ZOOM = 12;

    const instances = new Map();

    const destroy = (containerId) => {
        const instance = instances.get(containerId);
        if (!instance) {
            return;
        }
        instance.abort.abort();
        try {
            instance.map.remove();
        } catch (e) {
            console.warn('[MapPanel] remove failed', e);
        }
        instances.delete(containerId);
    };

    const loadStyle = async (styleUrl, tilesOverrideUrl) => {
        const response = await fetch(styleUrl);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status} on ${styleUrl}`);
        }
        const style = await response.json();

        if (tilesOverrideUrl) {
            for (const source of Object.values(style.sources ?? {})) {
                if (source?.type === 'vector') {
                    source.tiles = [tilesOverrideUrl];
                    delete source.url;
                }
            }
        }
        return style;
    };

    const computeBounds = (points) => {
        const bounds = new maplibregl.LngLatBounds();
        for (const p of points) {
            bounds.extend([p.lng, p.lat]);
        }
        return bounds;
    };

    const addMarker = (map, point, callbackUrl, signal) => {
        const marker = new maplibregl.Marker({ color: point.color ?? '#3498db' })
            .setLngLat([point.lng, point.lat])
            .addTo(map);

        const el = marker.getElement();
        const label = document.createElement('div');
        label.className = 'map-marker-label';
        label.textContent = point.label ?? '';
        el.appendChild(label);

        if (callbackUrl) {
            el.style.cursor = 'pointer';
            el.addEventListener('click', (e) => {
                e.stopPropagation();
                globalThis.Wicket?.Ajax?.get({
                    u: `${callbackUrl}&pointId=${encodeURIComponent(point.id)}`,
                });
            }, { signal });
        }

        return marker;
    };

    const clearMarkers = (instance) => {
        for (const marker of instance.markers) {
            marker.remove();
        }
        instance.markers = [];
    };

    const applyPoints = (instance) => {
        clearMarkers(instance);
        for (const p of instance.points) {
            instance.markers.push(
                addMarker(instance.map, p, instance.callbackUrl, instance.abort.signal)
            );
        }
    };


    const installWicketHook = () => {
        if (!globalThis.Wicket?.Event || globalThis.MapPanel.isWicketHooked) { 
            return;
        }

        globalThis.Wicket.Event.subscribe('/dom/node/removing', (_jqEvent, element) => {
            if (!element) {
                return;
            }
			
            for (const id of instances.keys()) {
                const target = document.getElementById(id);
                if (!target) {
                    continue;
                }
                if (element === target || element.contains?.(target)) {
                    destroy(id);
                }
            }
        });

        globalThis.MapPanel.isWicketHooked = true;
    };

    globalThis.MapPanel ??= { isWicketHooked: false };
    globalThis.MapPanel.destroy = destroy;

    installWicketHook();

    globalThis.MapPanel.init = async (config) => {
        installWicketHook();

        destroy(config.containerId);

        if (!globalThis.maplibregl) {
            console.error('[MapPanel] maplibre-gl is not loaded');
            return null;
        }

        const container = document.getElementById(config.containerId);
        if (!container) {
           return null;
        }

        let style;
        try {
            style = await loadStyle(config.styleUrl, config.tilesOverrideUrl);
        } catch (err) {
            console.error('[MapPanel] failed to load style:', err);
            return null;
        }

        if (!document.body.contains(container)) {
            return null;
        }

        destroy(config.containerId);

        const abort = new AbortController();
        const points = config.points ?? [];
        const hasPoints = points.length > 0;

        const bounds = hasPoints
            ? computeBounds(points)
            : [
                  [config.fallbackSwLng, config.fallbackSwLat],
                  [config.fallbackNeLng, config.fallbackNeLat],
              ];

        const fitBoundsOptions = hasPoints
            ? { padding: FIT_PADDING_POINTS, maxZoom: FIT_MAX_ZOOM }
            : { padding: FIT_PADDING_FALLBACK };

        const map = new maplibregl.Map({
            container,
            style,
            attributionControl: { compact: true },
            bounds,
            fitBoundsOptions,
            renderWorldCopies: false
        });

        const instance = {
            map,
            abort,
            markers: [],
            points,
            callbackUrl: config.callbackUrl,
        };
        instances.set(config.containerId, instance);

        map.addControl(new maplibregl.ScaleControl({ unit: 'metric' }), 'bottom-left');

        map.on('load', () => {
            const attrib = map.getContainer().querySelector('.maplibregl-ctrl-attrib');
            if (attrib) {
                attrib.removeAttribute('open');
                attrib.classList.remove('maplibregl-compact-show');
            }
            applyPoints(instance);
        });

        map.on('error', (e) => console.error('[MapPanel] map error:', e?.error));

        return map;
    };

    globalThis.MapPanel.updatePoints = (containerId, points, callbackUrl) => {
        const instance = instances.get(containerId);
        if (!instance) {
            return;
        }

        instance.points = points ?? [];
        if (callbackUrl) {
            instance.callbackUrl = callbackUrl;
        }

        applyPoints(instance);
    };

    globalThis.MapPanel.centerOnPoint = (containerId, pointId) => {
        const instance = instances.get(containerId);
        if (!instance) {
            return;
        }

        const point = instance.points.find((p) => String(p.id) === String(pointId));
        if (!point) {
            return;
        }

        instance.map.easeTo({ center: [point.lng, point.lat] });
    };
}