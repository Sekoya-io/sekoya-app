{
    const FIT_PADDING_POINTS = 100;
    const FIT_PADDING_FALLBACK = 20;
    const FIT_MAX_ZOOM = 15;

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
        label.className = 'map-marker-label bg-glass';
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

        instances.set(config.containerId, { map, abort });

        map.addControl(new maplibregl.NavigationControl({ showCompass: false }), 'top-right');
        map.addControl(new maplibregl.ScaleControl({ unit: 'metric' }), 'bottom-left');

        map.on('load', () => {
            const attrib = map.getContainer().querySelector('.maplibregl-ctrl-attrib');
            if (attrib) {
                attrib.removeAttribute('open');
                attrib.classList.remove('maplibregl-compact-show');
            }
            for (const p of points) {
                addMarker(map, p, config.callbackUrl, abort.signal);
            }
        });

        map.on('error', (e) => console.error('[MapPanel] map error:', e?.error));

        return map;
    };
}