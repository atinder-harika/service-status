import StatusPage from './StatusPage';

const App = (): JSX.Element => {
  return (
    <div className="min-h-screen bg-neutral-900 text-neutral-200 p-4 sm:p-8">
      <div className="max-w-4xl mx-auto">
        <StatusPage />
      </div>
    </div>
  );
};

export default App;

