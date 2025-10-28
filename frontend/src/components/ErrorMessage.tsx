interface ErrorMessageProps {
  error: string;
  onRetry?: () => void;
}

/**
 * ErrorMessage Component - displays error state with retry button
 */
export default function ErrorMessage({ error, onRetry }: ErrorMessageProps) {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <div className="text-xl text-red-400 mb-4">Failed to load services</div>
        <div className="text-sm text-neutral-400 mb-4">{error}</div>
        {onRetry && (
          <button
            onClick={onRetry}
            className="px-4 py-2 bg-zinc-700 hover:bg-zinc-600 text-white rounded-lg transition-colors"
          >
            Retry
          </button>
        )}
      </div>
    </div>
  );
}
