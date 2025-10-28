interface LoadingSpinnerProps {
  message?: string;
}

/**
 * LoadingSpinner Component - displays loading state
 */
export default function LoadingSpinner({ message = 'Loading services...' }: LoadingSpinnerProps) {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <div className="text-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-neutral-300 mx-auto mb-4"></div>
        <div className="text-xl text-neutral-300">{message}</div>
      </div>
    </div>
  );
}
