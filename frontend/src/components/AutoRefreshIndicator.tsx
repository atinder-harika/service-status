import { POLLING_INTERVAL } from '../config/constants';

/**
 * AutoRefreshIndicator Component - shows polling status
 */
export default function AutoRefreshIndicator() {
  const intervalSeconds = POLLING_INTERVAL / 1000;

  return (
    <div className="text-center text-sm text-neutral-500 mb-8">
      Auto-refreshing every {intervalSeconds} seconds
    </div>
  );
}
